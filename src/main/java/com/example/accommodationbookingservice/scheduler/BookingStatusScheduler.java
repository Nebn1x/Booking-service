package com.example.accommodationbookingservice.scheduler;

import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import com.example.accommodationbookingservice.entity.payment.Payment;
import com.example.accommodationbookingservice.entity.payment.PaymentStatus;
import com.example.accommodationbookingservice.entity.telegram.TelegramChat;
import com.example.accommodationbookingservice.entity.user.RoleName;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.StripeSessionCancellationException;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.BookingStatusRepository;
import com.example.accommodationbookingservice.repository.PaymentRepository;
import com.example.accommodationbookingservice.repository.PaymentStatusRepository;
import com.example.accommodationbookingservice.repository.TelegramChatRepository;
import com.example.accommodationbookingservice.telegram.BookingNotificationBot;
import com.example.accommodationbookingservice.telegram.TelegramNotificationBuilder;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingStatusScheduler {
    private static final Long ONE_HOUR = 1L;
    private static final BookingStatus.BookingStatusName PENDING_STATUS
            = BookingStatus.BookingStatusName.PENDING;
    private static final BookingStatus.BookingStatusName CONFIRMED_STATUS
            = BookingStatus.BookingStatusName.CONFIRMED;
    private static final BookingStatus.BookingStatusName EXPIRED_STATUS
            = BookingStatus.BookingStatusName.EXPIRED;

    private static final BookingStatus.BookingStatusName CANCELED_STATUS
            = BookingStatus.BookingStatusName.CANCELED;

    private static final PaymentStatus.PaymentStatusName CANCELED_PAYMENT_STATUS
            = PaymentStatus.PaymentStatusName.CANCEL;
    private static final PaymentStatus.PaymentStatusName PENDING_PAYMENT_STATUS
            = PaymentStatus.PaymentStatusName.PENDING;
    private static final RoleName ADMIN_ROLE = RoleName.ADMIN;
    private static final String CANT_CANCEL_STRIPE_SESSION = "Cant cancel stripe session ";
    private static final String CAN_T_FIND_STATUS = "Can't find status ";

    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final BookingNotificationBot bookingBot;
    private final TelegramChatRepository telegramChatRepository;

    @Scheduled(cron = "0 0 10 * * *")
    public void ifCheckInDateIsToday() {
        LocalDate today = LocalDate.now();
        List<Booking> bookingList = bookingRepository.findAllByCheckInDate(today, CONFIRMED_STATUS);
        if (bookingList.isEmpty()) {
            return;
        }
        bookingList.forEach(booking -> bookingBot.sendMessage(
                booking.getUser().getEmail(),
                TelegramNotificationBuilder.generateUpcomingBookingReminder(booking)));
    }

    @Scheduled(cron = "0 00 12 * * *")
    @Transactional
    public void ifCheckOutDateIsToday() {
        LocalDate today = LocalDate.now();
        System.out.println(today);
        List<Booking> bookingList
                = bookingRepository.findByCheckOutDateAndStatus(today, CONFIRMED_STATUS);
        if (bookingList.isEmpty()) {
            getAdminsEmail().forEach(b ->
                    bookingBot.sendMessage(b,
                            TelegramNotificationBuilder.getNoExpiredBookingsMessage()));
            return;
        }
        updateBookingsStatus(bookingList, EXPIRED_STATUS);
        bookingList.forEach(booking -> bookingBot.sendMessage(
                booking.getUser().getEmail(), TelegramNotificationBuilder.bookingExpired(booking)));
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    @Transactional
    public void ifBookingUnconfirmedOneHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(ONE_HOUR);
        List<Booking> bookingList
                = bookingRepository.findAllByCreatedAtAndStatus(oneHourAgo, PENDING_STATUS);
        if (bookingList.isEmpty()) {
            return;
        }
        updateBookingsStatus(bookingList, CANCELED_STATUS);
        cancelPayments(bookingList);
        bookingList.forEach(booking -> bookingBot.sendMessage(
                booking.getUser().getEmail(),
                TelegramNotificationBuilder.bookingExpired(booking)));
    }

    private void updateBookingsStatus(List<Booking> bookingList,
                                      BookingStatus.BookingStatusName statusName) {
        BookingStatus canceled = bookingStatusRepository.findByName(statusName)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_FIND_STATUS + statusName));
        bookingList.forEach(booking -> booking.setStatus(canceled));
        bookingRepository.saveAll(bookingList);
    }

    private void cancelPayments(List<Booking> bookingList) {
        List<Long> bookingIds = bookingList.stream().map(Booking::getId).toList();
        PaymentStatus cancelStatus = paymentStatusRepository
                .findPaymentStatusByName(CANCELED_PAYMENT_STATUS);
        List<Payment> paymentList = paymentRepository
                .findAllByBookingIdInAndStatus_Name(bookingIds, PENDING_PAYMENT_STATUS);
        for (Payment payment : paymentList) {
            payment.setStatus(cancelStatus);
            try {
                Session sessionById = Session.retrieve(payment.getSessionId());
                sessionById.expire();
            } catch (StripeException e) {
                throw new StripeSessionCancellationException(CANT_CANCEL_STRIPE_SESSION
                        + e.getMessage());
            }
        }
        paymentRepository.saveAll(paymentList);
    }

    private List<Long> getAdminsEmail() {
        List<TelegramChat> allByUserRolesName =
                telegramChatRepository.findAllByUser_Roles_Role(ADMIN_ROLE);
        return allByUserRolesName.stream().map(TelegramChat::getChatId).toList();
    }
}

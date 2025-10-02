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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingStatusScheduler {

    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final BookingNotificationBot bookingBot;
    private final TelegramChatRepository telegramChatRepository;

    @Value("${scheduler.check-in-cron}")
    private String checkInCron;

    @Value("${scheduler.check-out-cron}")
    private String checkOutCron;

    @Value("${scheduler.unconfirmed-delay}")
    private long unconfirmedDelay;

    @Scheduled(cron = "${scheduler.check-in-cron}")
    public void ifCheckInDateIsToday() {
        LocalDate today = LocalDate.now();
        List<Booking> bookingList = bookingRepository.findAllByCheckInDate(today, BookingStatus.BookingStatusName.CONFIRMED);
        if (bookingList.isEmpty()) {
            return;
        }
        bookingList.forEach(booking -> bookingBot.sendMessage(
                booking.getUser().getEmail(),
                TelegramNotificationBuilder.generateUpcomingBookingReminder(booking)));
    }

    @Scheduled(cron = "${scheduler.check-out-cron}")
    @Transactional
    public void ifCheckOutDateIsToday() {
        LocalDate today = LocalDate.now();
        List<Booking> bookingList = bookingRepository.findByCheckOutDateAndStatus(today, BookingStatus.BookingStatusName.CONFIRMED);
        if (bookingList.isEmpty()) {
            getAdminsEmail().forEach(b ->
                    bookingBot.sendMessage(b,
                            TelegramNotificationBuilder.getNoExpiredBookingsMessage()));
            return;
        }
        updateBookingsStatus(bookingList, BookingStatus.BookingStatusName.EXPIRED);
        bookingList.forEach(booking -> bookingBot.sendMessage(
                booking.getUser().getEmail(), TelegramNotificationBuilder.bookingExpired(booking)));
    }

    @Scheduled(fixedDelayString = "${scheduler.unconfirmed-delay}")
    @Transactional
    public void ifBookingUnconfirmedOneHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1L);
        List<Booking> bookingList = bookingRepository.findAllByCreatedAtAndStatus(oneHourAgo, BookingStatus.BookingStatusName.PENDING);
        if (bookingList.isEmpty()) {
            return;
        }
        updateBookingsStatus(bookingList, BookingStatus.BookingStatusName.CANCELED);
        cancelPayments(bookingList);
        bookingList.forEach(booking -> bookingBot.sendMessage(
                booking.getUser().getEmail(),
                TelegramNotificationBuilder.bookingExpired(booking)));
    }

    private void updateBookingsStatus(List<Booking> bookingList, BookingStatus.BookingStatusName statusName) {
        BookingStatus status = bookingStatusRepository.findByName(statusName)
                .orElseThrow(() -> new EntityNotFoundException("Can't find status " + statusName));
        bookingList.forEach(booking -> booking.setStatus(status));
        bookingRepository.saveAll(bookingList);
    }

    private void cancelPayments(List<Booking> bookingList) {
        List<Long> bookingIds = bookingList.stream().map(Booking::getId).toList();
        PaymentStatus cancelStatus = paymentStatusRepository.findPaymentStatusByName(PaymentStatus.PaymentStatusName.CANCEL);
        List<Payment> paymentList = paymentRepository.findAllByBookingIdInAndStatus_Name(bookingIds, PaymentStatus.PaymentStatusName.PENDING);
        for (Payment payment : paymentList) {
            payment.setStatus(cancelStatus);
            try {
                Session sessionById = Session.retrieve(payment.getSessionId());
                sessionById.expire();
            } catch (StripeException e) {
                throw new StripeSessionCancellationException("Cant cancel stripe session " + e.getMessage());
            }
        }
        paymentRepository.saveAll(paymentList);
    }

    private List<Long> getAdminsEmail() {
        List<TelegramChat> allByUserRolesName = telegramChatRepository.findAllByUser_Roles_Role(RoleName.ADMIN);
        return allByUserRolesName.stream().map(TelegramChat::getChatId).toList();
    }
}
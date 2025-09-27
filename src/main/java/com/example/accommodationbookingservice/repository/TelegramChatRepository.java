package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.telegram.TelegramChat;
import com.example.accommodationbookingservice.entity.user.RoleName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {

    Optional<TelegramChat> getTelegramChatByUserEmail(String email);

    List<TelegramChat> findAllByUser_Roles_Role(RoleName roleName);
}

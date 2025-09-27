package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles r WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserById(Long id);

    boolean existsByEmail(String email);
}

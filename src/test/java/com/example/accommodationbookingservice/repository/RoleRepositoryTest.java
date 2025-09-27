package com.example.accommodationbookingservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.accommodationbookingservice.entity.user.Role;
import com.example.accommodationbookingservice.entity.user.RoleName;
import com.example.accommodationbookingservice.testutil.UserRoleUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    public static final RoleName ADMIN = RoleName.ADMIN;
    public static final RoleName CUSTOMER = RoleName.CUSTOMER;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should return role Admin")
    void findByRoleName_ExistingRoleAdmin_ShouldReturnRole() {
        Role expectedRole = UserRoleUtil.getRoleAdmin();

        Role actualRole = roleRepository.findByRoleName(ADMIN);

        assertThat(actualRole).usingRecursiveComparison().isEqualTo(expectedRole);
    }

    @Test
    @DisplayName("Should return role Customer")
    void findByRoleName_ExistingRoleCustomer_ShouldReturnRole() {
        Role expectedRole = UserRoleUtil.getRoleCustomer();

        Role actualRole = roleRepository.findByRoleName(CUSTOMER);

        assertThat(actualRole).usingRecursiveComparison().isEqualTo(expectedRole);
    }
}

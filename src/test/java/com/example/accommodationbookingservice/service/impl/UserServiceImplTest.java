package com.example.accommodationbookingservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingservice.dto.user.UserRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserUpdatePasswordRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRoleRequestDto;
import com.example.accommodationbookingservice.entity.user.Role;
import com.example.accommodationbookingservice.entity.user.RoleName;
import com.example.accommodationbookingservice.entity.user.User;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.IncorrectPasswordException;
import com.example.accommodationbookingservice.exception.RegistrationException;
import com.example.accommodationbookingservice.mapper.UserMapper;
import com.example.accommodationbookingservice.repository.RoleRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.testutil.UserRoleUtil;
import com.example.accommodationbookingservice.testutil.UserUtil;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private String email;
    private User user;
    private UserUpdateRoleRequestDto userUpdateRoleRequestDto;
    private UserResponseDto userResponseDto;
    private UserUpdateRequestDto userUpdateRequestDto;
    private UserUpdatePasswordRequestDto userUpdatePasswordRequestDto;
    private UserRequestDto userRequestDto;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        user = UserUtil.getUser();
        userResponseDto = UserUtil.getUserResponseDto();
        userUpdateRequestDto = UserUtil.getUserUpdateRequestDto();
        userUpdatePasswordRequestDto = UserUtil.getUserUpdatePasswordRequestDto();
        userUpdateRequestDto = UserUtil.getUserUpdateRequestDto();
        userRequestDto = UserUtil.getUserRequestDto();
        roleAdmin = UserRoleUtil.getRoleAdmin();
        userUpdateRoleRequestDto = UserUtil.getUserUpdateRoleRequestDto();
    }

    @Test
    @DisplayName("Should return user info when user exists")
    void getInfo_ExistingUser_ShouldReturnUserResponseDto() {
        UserResponseDto expectedUser = userResponseDto;

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);
        UserResponseDto actualUser = userService.getInfo(email);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user does not exist")
    void getInfo_NonExistingUser_ShouldThrowException() {
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> userService.getInfo(email));

        String expectedMessage = "Can't find user by email " + email;

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should update user info and return UserResponseDto")
    void update_ExistingUser_ShouldUpdateAndReturnDto() {
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(UserUtil.getUpdatedUser());

        UserResponseDto actualUser = userService.update(email, userUpdateRequestDto);

        assertNotEquals(user.getEmail(), actualUser.getEmail());
        assertNotEquals(user.getFirstName(), actualUser.getFirstName());
        assertNotEquals(user.getLastName(), actualUser.getLastName());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existing user")
    void update_NonExistingUser_ShouldThrowException() {
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> userService.update(email, userUpdateRequestDto));

        String expectedMessage = "Can't find user by email " + email;

        assertEquals(expectedMessage, actualException.getMessage());

    }

    @Test
    @DisplayName("Should update password when old password is correct and new passwords match")
    void updatePassword_ValidRequest_ShouldUpdatePassword() {
        final String encodedOldPassword = "$2a$10$abc123";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("oldPass"), anyString())).thenReturn(true);

        when(passwordEncoder.encode(userUpdatePasswordRequestDto.getNewPassword()))
                .thenReturn("encodedNewPassword");

        user.setPassword(encodedOldPassword);
        String expected = "encodedNewPassword";

        userService.updatePassword(email, userUpdatePasswordRequestDto);

        assertEquals(expected, user.getPassword());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user does not exist")
    void updatePassword_UserNotFound_ShouldThrowException() {
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> userService.updatePassword(email, userUpdatePasswordRequestDto));

        String expectedMessage = "Can't find user by email " + email;
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should throw IncorrectPasswordException when old password does not match")
    void updatePassword_InvalidOldPassword_ShouldThrowException() {
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userUpdatePasswordRequestDto.getOldPassword(),
                user.getPassword())).thenReturn(false);

        IncorrectPasswordException actualException = assertThrows(IncorrectPasswordException.class,
                () -> userService.updatePassword(email, userUpdatePasswordRequestDto));

        String expectedMessage = "Old password is incorrect.";

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should throw IncorrectPasswordException when "
            + "new password and confirmation do not match")
    void updatePassword_NewPasswordMismatch_ShouldThrowException() {
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userUpdatePasswordRequestDto.getOldPassword(),
                user.getPassword())).thenReturn(true);

        userUpdatePasswordRequestDto.setNewPassword("newPass1");
        userUpdatePasswordRequestDto.setConfirmPassword("newPass2");

        IncorrectPasswordException actualException = assertThrows(IncorrectPasswordException.class,
                () -> userService.updatePassword(email, userUpdatePasswordRequestDto));

        String expectedMessage = "New password and confirmation do not match";

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should update user roles and return UserResponseDto")
    void updateRoles_ExistingUser_ShouldUpdateAndReturnDto() {
        Long id = 1L;
        final UserResponseDto expectedUser = userResponseDto;

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(userUpdateRoleRequestDto.getRoleName()))
                .thenReturn(roleAdmin);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);
        user.setRoles(new HashSet<>(Set.of(roleAdmin)));

        UserResponseDto actualUser = userService.updateRoles(id, userUpdateRoleRequestDto);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user not found by ID")
    void updateRoles_NonExistingUser_ShouldThrowException() {
        Long userId = 1L;

        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> userService.updateRoles(userId, userUpdateRoleRequestDto));

        String expectedMessage = "Can't find user by id " + userId;
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should register new user and return UserResponseDto")
    void register_NewUser_ShouldRegisterAndReturnDto() {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());

        Role customerRole = new Role();
        customerRole.setRole(RoleName.CUSTOMER);

        UserResponseDto expectedResponse = userResponseDto;

        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleName(RoleName.CUSTOMER)).thenReturn(customerRole);
        when(userMapper.toModelWithoutPasswordAndRoles(userRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(expectedResponse);

        UserResponseDto actualResponse = userService.register(userRequestDto);

        assertEquals(expectedResponse, actualResponse);
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().contains(customerRole));
    }

    @Test
    @DisplayName("Should throw RegistrationException when user with email already exists")
    void register_ExistingEmail_ShouldThrowException() {
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userService.register(userRequestDto));

        String expectedMessage = "User email already exist";

        assertEquals(expectedMessage, exception.getMessage());
    }
}

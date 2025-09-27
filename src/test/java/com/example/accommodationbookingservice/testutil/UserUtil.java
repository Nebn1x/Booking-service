package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.user.UserRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserUpdatePasswordRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRoleRequestDto;
import com.example.accommodationbookingservice.entity.user.RoleName;
import com.example.accommodationbookingservice.entity.user.User;

public class UserUtil {

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setFirstName("TestUserName");
        user.setLastName("TestUserLastName");
        user.setRoles(UserRoleUtil.getRole());
        user.setPassword("123456");
        return user;
    }

    public static User getExpectedUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("customer@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRoles(UserRoleUtil.getRolesCustomer());
        user.setPassword("$2a$10$TTf3rmUV08Yn1XA9nJjVbOYunIW..BnsE.tifOomYpcEk6ke1wwPu");
        return user;
    }

    public static UserResponseDto getUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setEmail("test@gmail.com");
        userResponseDto.setFirstName("TestUserName");
        userResponseDto.setLastName("TestUserLastName");
        userResponseDto.setRoles(UserRoleUtil.getRolesTypeDto());
        return userResponseDto;
    }

    public static UserResponseDto getUserResponseDtoCustomer() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(3L);
        userResponseDto.setEmail("test@gmail.com");
        userResponseDto.setFirstName("TestUserName");
        userResponseDto.setLastName("TestUserLastName");
        userResponseDto.setRoles(UserRoleUtil.getRolesTypeDtoCustomer());
        return userResponseDto;
    }

    public static UserResponseDto getUserResponseDtoAdmin() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(3L);
        userResponseDto.setEmail("test@gmail.com");
        userResponseDto.setFirstName("TestUserName");
        userResponseDto.setLastName("TestUserLastName");
        userResponseDto.setRoles(UserRoleUtil.getRolesTypeDto());
        return userResponseDto;
    }

    public static UserResponseDto getUserResponseDto2() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setEmail("customer@example.com");
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setRoles(UserRoleUtil.getRolesTypeDtoCustomer());
        return userResponseDto;
    }

    public static UserResponseDto getUpdatedUser() {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setEmail("changedEmail@Gmail.com");
        user.setFirstName("changedFirstName");
        user.setLastName("changedLastName");
        return user;
    }

    public static UserResponseDto getUpdatedUserWithRole() {
        UserResponseDto user = new UserResponseDto();
        user.setId(3L);
        user.setEmail("changedEmail@Gmail.com");
        user.setFirstName("changedFirstName");
        user.setLastName("changedLastName");
        user.setRoles(UserRoleUtil.getRolesTypeDtoCustomer());
        return user;
    }

    public static UserUpdateRequestDto getUserUpdateRequestDto() {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setEmail("changedEmail@Gmail.com");
        userUpdateRequestDto.setFirstName("changedFirstName");
        userUpdateRequestDto.setLastName("changedLastName");

        return userUpdateRequestDto;
    }

    public static UserUpdatePasswordRequestDto getUserUpdatePasswordRequestDto() {
        UserUpdatePasswordRequestDto updatePassword = new UserUpdatePasswordRequestDto();
        updatePassword.setOldPassword("oldPass");
        updatePassword.setNewPassword("newPass");
        updatePassword.setConfirmPassword("newPass");

        return updatePassword;
    }

    public static UserUpdatePasswordRequestDto getUserUpdateHashedPasswordRequestDto() {
        UserUpdatePasswordRequestDto updatePassword = new UserUpdatePasswordRequestDto();
        updatePassword.setOldPassword("oldPassword");
        updatePassword.setNewPassword("newPass123");
        updatePassword.setConfirmPassword("newPass123");

        return updatePassword;
    }

    public static UserUpdateRoleRequestDto getUserUpdateRoleRequestDto() {
        UserUpdateRoleRequestDto user = new UserUpdateRoleRequestDto();
        user.setRoleName(RoleName.ADMIN);

        return user;
    }

    public static UserRequestDto getUserRequestDto() {
        UserRequestDto user = new UserRequestDto();
        user.setEmail("test@gmail.com");
        user.setFirstName("TestUserName");
        user.setLastName("TestUserLastName");
        user.setPassword("123456");
        user.setRepeatPassword("123456");
        return user;
    }
}

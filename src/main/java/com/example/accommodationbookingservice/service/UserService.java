package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.user.UserRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserUpdatePasswordRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRoleRequestDto;

public interface UserService {

    UserResponseDto getInfo(String email);

    UserResponseDto update(String email,
                               UserUpdateRequestDto requestDto);

    void updatePassword(String email, UserUpdatePasswordRequestDto requestDto);

    UserResponseDto updateRoles(Long id, UserUpdateRoleRequestDto requestDto);

    UserResponseDto register(UserRequestDto requestDto);
}

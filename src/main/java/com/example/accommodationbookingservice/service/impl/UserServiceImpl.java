package com.example.accommodationbookingservice.service.impl;

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
import com.example.accommodationbookingservice.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final String CAN_T_REGISTER_USER_MSG = "User email already exist";
    private static final String CAN_T_FIND_USER_BY_EMAIL = "Can't find user by email ";
    private static final String OLD_PASSWORD_IS_INCORRECT = "Old password is incorrect.";
    private static final String PASSWORD_AND_CONFIRMATION_DO_NOT_MATCH =
            "New password and confirmation do not match";
    private static final String CAN_T_FIND_USER_BY_ID = "Can't find user by id ";
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto getInfo(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_FIND_USER_BY_EMAIL + email));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto update(String email, UserUpdateRequestDto requestDto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_FIND_USER_BY_EMAIL + email));
        userMapper.setUpdateInfoToUser(user, requestDto);
        return userMapper.toResponseDto(user);
    }

    @Override
    public void updatePassword(String email, UserUpdatePasswordRequestDto requestDto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_FIND_USER_BY_EMAIL + email));

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException(OLD_PASSWORD_IS_INCORRECT);
        }
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new IncorrectPasswordException(PASSWORD_AND_CONFIRMATION_DO_NOT_MATCH);
        }
        String encode = passwordEncoder.encode(requestDto.getNewPassword());
        user.setPassword(encode);
    }

    @Override
    public UserResponseDto updateRoles(Long id, UserUpdateRoleRequestDto requestDto) {
        User user = userRepository.findUserById(id).orElseThrow(() ->
                new EntityNotFoundException(CAN_T_FIND_USER_BY_ID + id));
        Role newRoleName = roleRepository.findByRoleName(requestDto.getRoleName());
        user.getRoles().clear();
        user.setRoles(Set.of(newRoleName));

        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto register(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(CAN_T_REGISTER_USER_MSG);
        }
        Role role = roleRepository.findByRoleName(RoleName.CUSTOMER);
        User user = userMapper.toModelWithoutPasswordAndRoles(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }
}

package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.roletype.RoleTypeDto;
import com.example.accommodationbookingservice.dto.user.UserRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingservice.entity.user.Role;
import com.example.accommodationbookingservice.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    @Mapping(source = "role", target = "name")
    RoleTypeDto toRoleDto(Role role);

    UserResponseDto toResponseDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toModelWithoutPasswordAndRoles(UserRequestDto requestDto);

    void setUpdateInfoToUser(@MappingTarget User user, UserUpdateRequestDto requestDto);
}

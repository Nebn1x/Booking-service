package com.example.accommodationbookingservice.dto.user;

import com.example.accommodationbookingservice.entity.user.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRoleRequestDto {

    @NotNull
    private RoleName roleName;
}

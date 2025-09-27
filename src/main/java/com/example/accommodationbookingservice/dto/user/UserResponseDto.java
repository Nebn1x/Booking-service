package com.example.accommodationbookingservice.dto.user;

import com.example.accommodationbookingservice.dto.roletype.RoleTypeDto;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<RoleTypeDto> roles;
}

package ru.lot.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.lot.enums.RoleType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private String email;
    private String password;
}

package ru.lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;
import ru.lot.dto.AuthorizationDTO;
import ru.lot.dto.RegistrationDTO;
import ru.lot.entity.User;
import ru.lot.enums.RoleType;
import ru.lot.security.JwtTokenProvider;
import ru.lot.service.UserService;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody AuthorizationDTO authorizationDto, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        User user = (User) userService.loadUserByUsername(authorizationDto.getEmail());
        if (Hashing.sha256()
                .hashString(authorizationDto.getPassword(), StandardCharsets.UTF_8).toString().equals(user.getPassword())) {
            String jwtValue = jwtTokenProvider.createJWT(user.getId(), user.getUsername(), user.getPassword());
            httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, jwtValue);
            log.info("User {} logged in", user.getUsername());
        } else {
            throw new AuthorizationServiceException("Invalid credentials");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    @Transactional
    public void registerNewUser(@RequestBody RegistrationDTO registrationDTO) throws Exception {
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setRole(RoleType.USER);
        user.setPassword(Hashing.sha256()
                .hashString(registrationDTO.getPassword(), StandardCharsets.UTF_8)
                .toString());
        user.setBalance(0d);
        user = userService.saveNewUser(user);
        log.info("Registered  {} : {}", user.getId(), registrationDTO.getEmail());
    }
}

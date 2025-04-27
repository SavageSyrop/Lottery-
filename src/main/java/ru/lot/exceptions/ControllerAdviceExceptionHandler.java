package ru.lot.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.lot.service.MailService;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceExceptionHandler {

    private MailService mailService;

    @Autowired
    public ControllerAdviceExceptionHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionBody handleDataIntegrityViolation(DataIntegrityViolationException e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFound(EntityNotFoundException e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalState(IllegalStateException e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class, ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAccessDenied(RuntimeException e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolation(ConstraintViolationException e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        exceptionBody.setErrors(e.getConstraintViolations().stream().collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)));
        return exceptionBody;
    }

    @ExceptionHandler(value = {AuthenticationException.class, AuthorizationServiceException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAuthentication(RuntimeException e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(Exception e) {
        e.printStackTrace();
        log.warn(e.toString());
        mailService.sendAdminError(e);
        return new ExceptionBody("Internal error: " + e.getMessage());
    }
}

package com.np.suprimpoudel.spring_mail_sender.exception.handler;

import com.np.suprimpoudel.spring_mail_sender.dto.Base;
import com.np.suprimpoudel.spring_mail_sender.dto.SubError;
import com.np.suprimpoudel.spring_mail_sender.exception.BusinessException;
import com.np.suprimpoudel.spring_mail_sender.util.constant.ResponseConstant;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Base<Void> handleRuntimeException(RuntimeException exception) {
        Base<Void> customException = new Base<>();
        customException.setMessage(exception.getMessage());
        return customException;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Base<Void> handleGeneralException(Exception exception) {
        Base<Void> customException = new Base<>();
        customException.setMessage(exception.getMessage());
        return customException;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public Base<Object> handleBusinessException(BusinessException exception) {
        Base<Object> customException = new Base<>();
        customException.setMessage(exception.getMessage());
        customException.setData(exception.getData());
        if (exception.getSubErrors() != null && !exception.getSubErrors().isEmpty()) {
            customException.setErrors(exception.getSubErrors());
        }
        return customException;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Base<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Base<Void> customException = new Base<>();
        customException.setMessage(ResponseConstant.INVALID_ARGUMENTS);

        List<SubError> customExceptionList = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> customExceptionList.add(new SubError(error.getField(), error.getDefaultMessage())));

        customException.setErrors(customExceptionList);
        return customException;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Base<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        Base<Void> customException = new Base<>();
        customException.setMessage(ResponseConstant.INVALID_ARGUMENTS);

        List<SubError> customExceptionList = new ArrayList<>();
        exception.getConstraintViolations().forEach(error -> {
            customExceptionList.add(new SubError(error.getPropertyPath().toString(), error.getMessage()));
        });

        customException.setErrors(customExceptionList);
        return customException;
    }
}
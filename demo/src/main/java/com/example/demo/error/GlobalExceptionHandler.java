package com.example.demo.error;

import com.example.demo.discord.DiscordMessageProvider;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.service.CustomUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonParseException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final DiscordMessageProvider discordMessageProvider;
    private final CustomUserDetailsService customUserDetailsService;


    private UserInfo getUserInfo() {
        String username = customUserDetailsService.getCurrentAuthenticatedMemberName();
        String UUID = customUserDetailsService.getCurrentAuthenticatedMemberUUID();
        MemberRole role = customUserDetailsService.getCurrentAuthenticatedMemberRole();
        return new UserInfo(username, UUID, role);
    }

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param ex HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException ex) {
        log.error("HttpClientErrorException.BadRequest", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param ex NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException ex) {
        log.error("handleNoHandlerFoundExceptionException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param ex NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error("handleNullPointerException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("handleUsernameNotFoundException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FORBIDDEN_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param ex IOException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * com.google.gson 내에 Exception 발생하는 경우
     *
     * @param ex JsonParseException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex) {
        log.error("handleJsonParseExceptionException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.JSON_PARSE_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleFeignClientException(FeignException.FeignClientException ex) {
        log.error("handleFeignClientException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FEIGN_CLIENT_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // ==================================================================================================================

    /**
     * [Exception] 모든 Runtime Exception 경우 발생
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("RuntimeException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
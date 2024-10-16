package com.teamdears.core.error;

import com.teamdears.core.discord.DiscordMessageProvider;
import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.error.custom.InvalidJwtAuthenticationException;
import com.teamdears.core.error.custom.PortfolioNotFoundException;
import com.teamdears.core.member.service.CustomUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonParseException;
import feign.FeignException;
import io.jsonwebtoken.MalformedJwtException;
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

import javax.security.auth.RefreshFailedException;
import java.io.IOException;
import java.security.SignatureException;

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

        if (username == null || UUID == null || role == null) {
            return new UserInfo("Unknown", "Unknown", MemberRole.UNKNOWN);
        }
        return new UserInfo(username, UUID, role);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponseAndSendAlert(Exception ex, ErrorCode errorCode, HttpStatus httpStatus) {
        log.error("{}", ex.getMessage(), ex);
        final ErrorResponse response = ErrorResponse.of(errorCode, ex.getMessage());
        UserInfo userInfo = getUserInfo();

        discordMessageProvider.sendExceptionMessage(userInfo, response, ex);
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }

        return buildErrorResponseAndSendAlert(ex, ErrorCode.NOT_VALID_ERROR, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.NOT_VALID_HEADER_ERROR, HttpStatus.BAD_REQUEST);
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
        return buildErrorResponseAndSendAlert(ex, ErrorCode.REQUEST_BODY_MISSING_ERROR, HttpStatus.BAD_REQUEST);
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
        return buildErrorResponseAndSendAlert(ex, ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param ex HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.BAD_REQUEST_ERROR, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param ex NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.NOT_VALID_ERROR, HttpStatus.NOT_FOUND);
    }


    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param ex NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.NULL_POINT_ERROR, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.USERNAME_NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param ex IOException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.IO_ERROR, HttpStatus.BAD_REQUEST);
    }


    /**
     * com.google.gson 내에 Exception 발생하는 경우
     *
     * @param ex JsonParseException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.JSON_PARSE_ERROR, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param ex RefreshFailedException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(RefreshFailedException.class)
    protected ResponseEntity<ErrorResponse> handleRefreshFailedException(RefreshFailedException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.REFRESH_TOKEN_EXPIRED_ERROR, HttpStatus.BAD_REQUEST);
    }

    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.JACKSON_PROCESS_ERROR, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleFeignClientException(FeignException.FeignClientException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.FEIGN_CLIENT_ERROR, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] JWT 토큰이 유효하지 않은 경우
     *
     * @param ex MalformedJwtException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PortfolioNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlePortfolioNotFoundException(PortfolioNotFoundException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.PORTFOLIO_NOT_FOUND_ERROR, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidJwtAuthentication(InvalidJwtAuthenticationException ex) {
        return buildErrorResponseAndSendAlert(ex, ErrorCode.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED);
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
        return buildErrorResponseAndSendAlert(ex, ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
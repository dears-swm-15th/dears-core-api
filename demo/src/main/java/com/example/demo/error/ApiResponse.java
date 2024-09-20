package com.example.demo.error;

import lombok.Builder;
import lombok.Getter;

/**
 * [공통] API Response 결과의 반환 값을 관리
 */
@Getter
public class ApiResponse<T> {

    // API 응답 결과 Response(Generic)
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static <T> ApiResponse<T> of(int resultCode, String resultMsg, T result) {
        return new ApiResponse<>(result, resultCode, resultMsg);
    }

}
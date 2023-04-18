package com.almostThere.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.FieldError;

public class ErrorResponse {

    private String message;
    private int code;
    
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)  //만약 에러 필드가 null 이라면 response에서 무시
    private List<FieldError> errors; //DTO validation 처리 시 각 필드에 대한 error를 담을 리스트


    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }


}

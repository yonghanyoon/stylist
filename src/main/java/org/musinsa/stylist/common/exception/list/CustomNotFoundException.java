package org.musinsa.stylist.common.exception.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomNotFoundException extends RuntimeException {

    private HttpStatus errorCode;

    public CustomNotFoundException(HttpStatus errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
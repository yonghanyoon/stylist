package org.musinsa.stylist.common.exception.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomBadRequestException extends RuntimeException{

    public CustomBadRequestException(String message) {
        super(message);
    }
}

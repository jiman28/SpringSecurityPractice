package com.ji.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
    public DataNotFoundException(String message) {
        super(message);
    }
    // DataNotFoundException은 RuntimeException을 상속하여 만들었다.
    // 만약 DataNotFoundException이 발생하면 @ResponseStatus 에 의해 404 오류(HttpStatus.NOT_FOUND)가 나타날 것이다.
}

package com.aston.notification_service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Getter @Setter
    private boolean isSuccessful;

    @Getter @Setter
    private int statusCode;

    @Getter @Setter
    private String message;

    @Getter @Setter
    private Object data;
}

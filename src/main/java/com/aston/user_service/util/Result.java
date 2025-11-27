package com.aston.user_service.util;

import com.aston.user_service.controller.UserController;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Getter @Setter
    private boolean isSuccessful;

    @Getter @Setter
    private int statusCode;

    @Getter @Setter
    private String message;

    @Getter @Setter
    private Object data;

    @PrePersist
    public void log() {
        if (isSuccessful) logger.info(this.message);
    }
}

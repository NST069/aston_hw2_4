package com.aston.user_service.service;

import com.aston.user_service.mapper.EmailDTO;

public interface NotificationService {

    boolean sendEmailToNotificationService(EmailDTO emailDTO);
}

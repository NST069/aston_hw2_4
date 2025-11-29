package com.aston.notification_service.service;

import com.aston.notification_service.mapper.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MailSenderService mailSenderService;

    private final MessageSource messageSource;

    public void sendEmail(EmailDTO emailDTO) {
        String code = "email.text." + emailDTO.event();
        mailSenderService.send(emailDTO.to(), emailDTO.subject(), messageSource.getMessage(code, null, Locale.getDefault()));
    }
}

package com.bix.image_processor.image_processing;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to,
                                        String subject,
                                        String body,
                                        String imageFileName,
                                        byte[] image) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(imageFileName, new ByteArrayResource(image));
        };
        mailSender.send(preparator);
    }
}

package usermanagement.service.kafka;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import usermanagement.dto.EmailMessage;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final EmailService emailConsumer;

    @KafkaListener(topics = "email_topic", groupId = "email_group")
    public void consumeEmailMessage(EmailMessage emailMessage) throws MessagingException {
        emailConsumer.sendEmail(emailMessage);
        System.out.println("Email sent successfully");
    }

}
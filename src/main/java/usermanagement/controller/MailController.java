package usermanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usermanagement.dto.EmailMessage;
import usermanagement.service.kafka.ProducerService;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final ProducerService producer;

    @PostMapping("/send")
    ResponseEntity<?> sendMail(@RequestBody EmailMessage emailMessage) {
        try {
            producer.sendMessage(emailMessage);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

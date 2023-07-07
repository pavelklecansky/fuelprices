package cz.klecansky.gasprices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FuelPriceMailSendingService {

    @Value("${email.address.to}")
    private String toEmailAddress;
    @Value("${email.address.from}")
    private String fromEmailAddress;

    private final MailSender mailSender;

    public FuelPriceMailSendingService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMailMessage() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmailAddress);
        simpleMailMessage.setTo(toEmailAddress);
        simpleMailMessage.setSubject("Fuel Prices: " + LocalDateTime.now().format(dateFormat));
        simpleMailMessage.setText("test content");
        this.mailSender.send(simpleMailMessage);
    }
}

package cz.klecansky.gasprices;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class FuelPriceMailSendingService {

    private final Logger logger = LoggerFactory.getLogger(FuelPriceMailSendingService.class);

    @Value("${email.address.to}")
    private String toEmailAddress;
    @Value("${email.address.from}")
    private String fromEmailAddress;


    private final FuelPricesEmailTemplate fuelPricesEmailTemplate;
    private final JavaMailSender mailSender;

    public FuelPriceMailSendingService(FuelPricesEmailTemplate fuelPricesEmailTemplate, JavaMailSender mailSender) {
        this.fuelPricesEmailTemplate = fuelPricesEmailTemplate;
        this.mailSender = mailSender;
    }

    public void sendMailFuelPrices(Map<Country, CountryFuelPrice> prices) throws MessagingException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom(fromEmailAddress);
        logger.info("From Email Address: " + fromEmailAddress);
        helper.setTo(toEmailAddress);
        logger.info("To Email Address: " + toEmailAddress);
        helper.setSubject("Fuel Prices: " + LocalDateTime.now().format(dateFormat));
        String emailHTML = fuelPricesEmailTemplate.emailHTML(prices);
        logger.info("Email body: \n" + emailHTML);
        helper.setText(emailHTML, true);
        this.mailSender.send(mimeMessage);
    }
}

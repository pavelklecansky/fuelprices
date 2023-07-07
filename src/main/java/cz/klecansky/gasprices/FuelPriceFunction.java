package cz.klecansky.gasprices;


import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class FuelPriceFunction implements Supplier<Map<Country, CountryFuelPrice>> {
    private final Logger logger = LoggerFactory.getLogger(FuelPriceFunction.class);

    private final FuelPriceService fuelPriceService;
    private final FuelPriceMailSendingService fuelPriceMailSendingService;

    public FuelPriceFunction(FuelPriceService fuelPriceService, FuelPriceMailSendingService fuelPriceMailSendingService) {
        this.fuelPriceService = fuelPriceService;
        this.fuelPriceMailSendingService = fuelPriceMailSendingService;
    }

    @Override
    public Map<Country, CountryFuelPrice> get() {
        Map<Country, CountryFuelPrice> countryCountryFuelPriceMap = fuelPriceService.allCountriesFuelPrices();
        try {
            fuelPriceMailSendingService.sendMailFuelPrices(countryCountryFuelPriceMap);
            logger.info("Email was sent.");
        } catch (MessagingException e) {
            logger.error("Error when sending email: ", e);
            throw new RuntimeException(e);
        }
        return countryCountryFuelPriceMap;
    }
}

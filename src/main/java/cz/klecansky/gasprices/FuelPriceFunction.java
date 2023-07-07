package cz.klecansky.gasprices;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class FuelPriceFunction implements Supplier<Map<Country, CountryFuelPrice>> {

    private final FuelPriceService fuelPriceService;
    private final FuelPriceMailSendingService fuelPriceMailSendingService;

    public FuelPriceFunction(FuelPriceService fuelPriceService, FuelPriceMailSendingService fuelPriceMailSendingService) {
        this.fuelPriceService = fuelPriceService;
        this.fuelPriceMailSendingService = fuelPriceMailSendingService;
    }

    @Override
    public Map<Country, CountryFuelPrice> get() {
        Map<Country, CountryFuelPrice> countryCountryFuelPriceMap = fuelPriceService.allCountriesFuelPrices();
        fuelPriceMailSendingService.sendMailMessage();
        return countryCountryFuelPriceMap;
    }
}

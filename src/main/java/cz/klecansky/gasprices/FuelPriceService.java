package cz.klecansky.gasprices;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FuelPriceService {
    private final Logger logger = LoggerFactory.getLogger(FuelPriceService.class);

    public static final Map<Country, String> COUNTRIES_FUEL_PRICES_URLS = Map.ofEntries(
            Map.entry(Country.CZ, "https://www.mylpg.eu/stations/czech-republic/"),
            Map.entry(Country.FR, "https://www.mylpg.eu/stations/france/"),
            Map.entry(Country.HR, "https://www.mylpg.eu/stations/croatia/"),
            Map.entry(Country.IT, "https://www.mylpg.eu/stations/italy/"),
            Map.entry(Country.HU, "https://www.mylpg.eu/stations/hungary/"),
            Map.entry(Country.DE, "https://www.mylpg.eu/stations/germany/"),
            Map.entry(Country.PL, "https://www.mylpg.eu/stations/poland/"),
            Map.entry(Country.AT, "https://www.mylpg.eu/stations/austria/"),
            Map.entry(Country.GR, "https://www.mylpg.eu/stations/greece/"),
            Map.entry(Country.SK, "https://www.mylpg.eu/stations/slovakia/"),
            Map.entry(Country.SI, "https://www.mylpg.eu/stations/slovenia/"),
            Map.entry(Country.ES, "https://www.mylpg.eu/stations/spain/"),
            Map.entry(Country.CH, "https://www.mylpg.eu/stations/switzerland/"));

    public Map<Country, CountryFuelPrice> allCountriesFuelPrices() {
        return COUNTRIES_FUEL_PRICES_URLS.keySet().stream()
                .map(s -> {
                    try {
                        Thread.sleep(1000);
                        return Map.entry(s, countryFuelPrice(s));
                    } catch (Exception ex) {
                        logger.error("Error when getting fuel prices for: " + s, ex);
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public CountryFuelPrice countryFuelPrice(Country country) throws IOException {
        Document doc = Jsoup.connect(COUNTRIES_FUEL_PRICES_URLS.get(country)).get();
        logger.info("Load " + country.name() + " fuel prices page with title: " + doc.title());
        Price gasolinePrice = extractGasolinePrice(doc);
        logger.info("Gasoline price: " + gasolinePrice);
        Price dieselPrice = extractDieselPrice(doc);
        logger.info("Gasoline price: " + dieselPrice);
        Price LPGPrice = extractLPGPrice(doc);
        logger.info("Gasoline price: " + LPGPrice);
        return new CountryFuelPrice(country, gasolinePrice, dieselPrice, LPGPrice);
    }

    private Price extractGasolinePrice(Document document) {
        String extractedGasolinePrice = document.select("body > div:nth-child(3) > div:nth-child(4) > div > table > tbody > tr:nth-child(2) > td:nth-child(2) > span").text();
        return extractPriceFromText(extractedGasolinePrice);
    }

    private Price extractLPGPrice(Document document) {
        String extractedLPGPrice = document.select("body > div:nth-child(3) > div:nth-child(4) > div > table > tbody > tr:nth-child(2) > td:nth-child(1) > span").text();
        return extractPriceFromText(extractedLPGPrice);
    }

    private Price extractDieselPrice(Document document) {
        String extractedDieselPrice = document.select("body > div:nth-child(3) > div:nth-child(4) > div > table > tbody > tr:nth-child(2) > td:nth-child(3) > span").text();
        return extractPriceFromText(extractedDieselPrice);
    }

    private Price extractPriceFromText(String text) {
        if (text == null || text.isEmpty() || Objects.equals(text.trim(), "N / A")) {
            return new Price(Double.NaN, Monetary.getCurrency("EUR"));
        }
        String[] extractedPrice = text.split(" ");
        double price = tryParseDouble(extractedPrice[0]);
        CurrencyUnit currency = Monetary.getCurrency(extractedPrice[1].substring(0, extractedPrice[1].length() - 2));
        return new Price(price, currency);
    }

    /**
     * Try to parse double if fail return NaN
     */
    private double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}

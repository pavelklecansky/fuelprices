package cz.klecansky.gasprices;

public record CountryFuelPrice(Country country, Price gasolinePrice, Price dieselPrice, Price LPGPrice) {
}

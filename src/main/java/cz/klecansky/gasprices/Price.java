package cz.klecansky.gasprices;

import javax.money.CurrencyUnit;

public record Price(double price, CurrencyUnit currency) {
}

@import java.util.Comparator
@import java.util.Map
@import java.util.stream.Collectors
@import cz.klecansky.gasprices.*

@param Map<Country, CountryFuelPrice> prices


<!DOCTYPE html>
<html>
<head>
    <title>Page Title</title>
</head>

<body>

<h1>Fuel prices</h1>

<table>
    <tr>
        <th style="padding: 0 5px;"></th>
        <th style="padding: 0 5px;">Diesel</th>
        <th style="padding: 0 5px;">LPG</th>
        <th style="padding: 0 5px;">Benzín</th>
    </tr>
    @for(Map.Entry<Country, CountryFuelPrice> entry : prices.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList())
        <tr>
            <td style="padding: 0 5px;">${entry.getKey().getName()}</td>
            <td style="padding: 0 5px;">${entry.getValue().dieselPrice().price()} ${entry.getValue().dieselPrice().currency().getCurrencyCode()}</td>
            <td style="padding: 0 5px;">${entry.getValue().LPGPrice().price()} ${entry.getValue().LPGPrice().currency().getCurrencyCode()}</td>
            <td style="padding: 0 5px;">${entry.getValue().gasolinePrice().price()} ${entry.getValue().gasolinePrice().currency().getCurrencyCode()}</td>
        </tr>
    @endfor
</table>
</body>
</html>


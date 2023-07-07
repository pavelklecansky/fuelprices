package cz.klecansky.gasprices;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FuelPricesEmailTemplate {

    private final TemplateEngine jteTemplateEngine;

    public FuelPricesEmailTemplate(TemplateEngine jteTemplateEngine) {
        this.jteTemplateEngine = jteTemplateEngine;
    }

    public String emailHTML(Map<Country, CountryFuelPrice> prices) {
        var output = new StringOutput();
        jteTemplateEngine.render("email.jte", prices, output);
        return output.toString();
    }
}

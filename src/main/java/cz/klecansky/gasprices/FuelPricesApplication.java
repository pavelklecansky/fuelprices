package cz.klecansky.gasprices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FuelPricesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuelPricesApplication.class, args);
	}
}

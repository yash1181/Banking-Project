package com.axis.AxisBank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title="Axis Bank App",
				description = "BackEnd REST API'S for AXIS Bank.",
				version = "v1.1.0",
				contact = @Contact(
                name = "Yash Tajanpure",
				email = "yashtaj1811@gmail.com",
				url = "https://google.com"
        ),
		license = @License(
				name = "AXIS BANK",
				url = "https://google.com"
		)
		),
		externalDocs =  @ExternalDocumentation(
				description = "The AXIS Bank App Documentation",
				url = "https://google.com"
		)
)
public class AxisBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AxisBankApplication.class, args);
	}

}

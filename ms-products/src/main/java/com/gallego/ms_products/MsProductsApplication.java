package com.gallego.ms_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.gallego.ms_products", "com.gallego.ms_common"})
public class MsProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProductsApplication.class, args);
	}

}

package com.gallego.ms_users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.gallego.ms_users", "com.gallego.ms_common"})
public class MsUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsUsersApplication.class, args);
	}

}

package com.upo.wmdsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class WMdspApplication {

	public static void main(String[] args) {
		SpringApplication.run(WMdspApplication.class, args);
	}

	@GetMapping("/")
    public String home() {
        return "index";
    }

}

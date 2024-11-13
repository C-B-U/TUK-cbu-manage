package com.example.cbuManage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CbuManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CbuManageApplication.class, args);
	}

}

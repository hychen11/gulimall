package com.hychen11.thirdPart;
import com.hychen11.thirdPart.properties.SmsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {SmsProperties.class})
public class ThirdPartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThirdPartApplication.class, args);
	}

}

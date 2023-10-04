package br.com.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FeedBackApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedBackApiApplication.class, args);
	}

}

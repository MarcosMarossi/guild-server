package br.com.feira.guild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
@EnableDiscoveryClient
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

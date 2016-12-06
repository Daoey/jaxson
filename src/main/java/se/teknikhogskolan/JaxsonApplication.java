package se.teknikhogskolan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@ApplicationPath("jaxson")
public class JaxsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxsonApplication.class, args);
	}
}

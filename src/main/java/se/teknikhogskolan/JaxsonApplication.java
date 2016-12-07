package se.teknikhogskolan;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@ApplicationPath("jaxson")
@ComponentScan("se.teknikhogskolan")
public class JaxsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxsonApplication.class, args);
	}
}

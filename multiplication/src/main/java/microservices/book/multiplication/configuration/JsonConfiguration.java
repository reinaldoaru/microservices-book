package microservices.book.multiplication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;

@Configuration
public class JsonConfiguration {

    @Bean
    public Module hibernatModule() {
        return new Hibernate5JakartaModule();
    }
}

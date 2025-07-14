package org.springframework.samples.petclinic.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
class ObjectMapperConfig {

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModules(new Jdk8Module(), new JavaTimeModule());
	}

}

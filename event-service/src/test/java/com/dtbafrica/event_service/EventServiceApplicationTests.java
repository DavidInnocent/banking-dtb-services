package com.dtbafrica.event_service;

import com.dtbafrica.store_service.config.ProfileClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = {
		"spring.main.allow-bean-definition-overriding=true",
		"profile.service.url=http://localhost:8081"
})
@ImportAutoConfiguration
@ExtendWith(MockitoExtension.class)
class EventServiceApplicationTests {
	
	@Mock
	private ProfileClient profileClient;
	@Test
	void contextLoads() {
	}

}

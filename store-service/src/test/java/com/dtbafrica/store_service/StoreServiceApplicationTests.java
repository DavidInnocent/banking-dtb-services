package com.dtbafrica.store_service;

import com.dtbafrica.store_service.config.ProfileClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StoreServiceApplicationTests {
	
	@Mock
	private ProfileClient profileClient;
	@Test
	void contextLoads() {
	}

}

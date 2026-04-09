package com.appointmentscheduler.eventprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.config.import=optional:configserver:")
class EventprocessorApplicationTests {

	@Test
	void contextLoads() {
	}

}

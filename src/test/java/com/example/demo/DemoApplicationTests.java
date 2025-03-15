package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = DemoApplication.class)
@TestPropertySource(locations = "classpath:application.yml")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}

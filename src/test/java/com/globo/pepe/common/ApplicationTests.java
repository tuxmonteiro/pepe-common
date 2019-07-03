package com.globo.pepe.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {
    "pepe.logging.tags=default",
    "cors.allowed-origins=http://localhost",
    "cors.allowed-methods=GET,POST,DELETE,PATCH,PUT,HEAD,OPTIONS,TRACE,CONNECT"
})
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}

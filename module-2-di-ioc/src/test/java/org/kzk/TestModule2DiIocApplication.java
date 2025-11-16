package org.kzk;

import org.springframework.boot.SpringApplication;

public class TestModule2DiIocApplication {

	public static void main(String[] args) {
		SpringApplication.from(Module2DiIocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

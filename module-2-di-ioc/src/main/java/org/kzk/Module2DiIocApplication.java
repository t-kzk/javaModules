package org.kzk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Module2DiIocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Module2DiIocApplication.class, args);

        int a = 0 ;
    }

}

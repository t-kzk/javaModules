package org.kzk.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Module2DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Module2DemoApplication.class, args);
        HelloWorld hello = context.getBean("hello", HelloWorld.class);
        String helloMsg = hello.getHelloMsg();
        hello.trySayAnotherWord();
        int a = 0 ;
    }

}

package org.kzk;

import org.kzk.data.WriterRepository;
import org.kzk.demo.HelloWorld;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Module2DiIocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Module2DiIocApplication.class, args);
        HelloWorld hello = context.getBean("hello", HelloWorld.class);
        String helloMsg = hello.getHelloMsg();

        int a = 0 ;
    }

}

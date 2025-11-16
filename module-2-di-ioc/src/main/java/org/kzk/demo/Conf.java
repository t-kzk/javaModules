package org.kzk.demo;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Conf {
    @Bean(name = "hello")
    @Scope(scopeName = "singleton")
    public HelloWorld helloWorld(ApplicationEventPublisher publisher) {
        HelloWorld hw = new HelloWorld(publisher);
        hw.setHelloMsg("Hello from @Bean config! FIRST!!!!!");
        return hw;
    }
    @Bean(name = "hello_2")
    @Scope(scopeName = "prototype")
    public HelloWorld helloWorld2(ApplicationEventPublisher publisher) {
        HelloWorld hw = new HelloWorld(publisher);
        hw.setHelloMsg("Hello from @Bean config! SECOND!!!!!!");
        return hw;
    }
}

package org.kzk.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

//@Component
public class HelloWorld {

    private String helloMsg = "default Hello";

    private final ApplicationEventPublisher publisher;

    @ValueForTest
    private String valueForTestBeanPostProcessor = "EMPTY!";

    @PostConstruct
    void init() {
        System.out.println("init");
    }

    @PreDestroy
    void destroy() {
        System.out.println("destroy");
    }

    public HelloWorld(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        System.out.println("CONSTRUCTOR!!!!!!");
    }


    public void setHelloMsg(String msg) {
        helloMsg = msg;
    }

    public String getHelloMsg() {
        System.out.println(helloMsg);
        return helloMsg;
    }

    public void trySayAnotherWord() {
        publisher.publishEvent("CUSTOM EVENT: trySayAnotherWord");
    }

    public String getValueForTestBeanPostProcessor() {
        return valueForTestBeanPostProcessor;
    }
}

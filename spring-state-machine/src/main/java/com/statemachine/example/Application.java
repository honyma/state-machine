package com.statemachine.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

/**
 * @author mazehong
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        StateMachine<States, Events> stateMachine = (StateMachine<States, Events>) context.getBean("stateMachineConfig_1");
        stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload(Events.EVENT1).setHeaderIfAbsent("name", "test1").build()))
                .subscribe();
        stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload(Events.EVENT2).setHeaderIfAbsent("name", "test2").build()))
                .subscribe();
    }
}

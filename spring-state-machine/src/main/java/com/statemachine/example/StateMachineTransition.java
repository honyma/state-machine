package com.statemachine.example;

import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author mazehong
 * @date 2023/1/16
 */
@Component("stateMachineTransition_1")
@WithStateMachine
public class StateMachineTransition {

    @OnTransition(source = "SI", target = "STATE1")
    public Boolean toState1(Message<Events> message) {
        System.out.println("收到状态机状态迁移事件：" + message.getPayload().name());
        return Boolean.TRUE;
    }

    @OnTransition(source = "STATE1", target = "STATE2")
    public Boolean toState2(Message<Events> message) {
        System.out.println("收到状态机状态迁移事件：" + message.getPayload().name());
        return Boolean.TRUE;
    }
}

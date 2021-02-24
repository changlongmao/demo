package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName: PushOrderEvent
 * @Description:
 * @Author: Chang
 * @Date: 2021/02/24 15:19
 **/
@Getter
public class PushOrderEvent extends ApplicationEvent {
    private String orderNo;

    private Long userId;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public PushOrderEvent(Object source, String orderNo,Long userId) {
        super(source);
        this.orderNo = orderNo;
        this.userId = userId;
    }

}

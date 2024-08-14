package org.asd.application;

import org.asd.frameWork.EventListener;

public class AddCustomerEvent {
    private String message;
    public AddCustomerEvent(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
package org.asd.application;

import org.asd.frameWork.EventListener;
import org.asd.frameWork.Service;

@Service
public class Listener2 {
    @EventListener
    public void onEvent(AddCustomerEvent event) {
        System.out.println("received event :" + event.getMessage());;
    }


}
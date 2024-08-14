package org.asd.application;

import org.asd.frameWork.EventListener;
import org.asd.frameWork.Service;

@Service
public class Listener {
    @EventListener
    public void onEvent(NewCustomerEvent event) {
        System.out.println("received event :" + event.getCustomer());;
    }


}
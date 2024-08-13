package miu.edu.application;


import miu.edu.framework.Service;

@Service
public class PaymentService {

    public PaymentService() {
    }
    public void pay(String message) {
        System.out.println( "PaymentService.pay() from "+message);
    }
}


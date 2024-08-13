package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Service;

@Service

public class VisaPayment implements IPaymentService {


    IPaymentService printService;


    @Autowired
    public void setPaymentService(IPaymentService paymentService) {
      this.printService = paymentService;
    }

public void pay() {
        System.out.println("From VisaPayment setter Injection");
    }
}

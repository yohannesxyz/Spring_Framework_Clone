package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Service;
import miu.edu.framework.Value;

@Service
public class PaypalPayment implements IPaymentService {

    @Value("paypal")
    private String type;
    public PaypalPayment() {
    }
    IPaymentService printService;
    @Autowired
    public PaypalPayment(IPaymentService paymentService) {
        this.printService = paymentService;

    }
    public void pay() {
        System.out.println("From PaypalPayment constructor Injection. This is the payment" +
                " value of "+ type);
    }


}





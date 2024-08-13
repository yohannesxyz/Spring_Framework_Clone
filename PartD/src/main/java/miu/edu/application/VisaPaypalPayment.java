package miu.edu.application;

import miu.edu.framework.Autowired;
import miu.edu.framework.Service;
import miu.edu.framework.Value;

@Service
public class VisaPaypalPayment implements IPaymentService {
    @Autowired
    IPaymentService printService;

    @Value("card")
    private String card;
    public void pay() {
       System.out.println("From VisaPaypalPayment Field Injection. And the card is "+ card);
    }
}

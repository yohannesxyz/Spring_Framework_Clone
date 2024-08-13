package miu.edu.application;

import miu.edu.framework.Qualifier;
import miu.edu.framework.Service;
import miu.edu.framework.Value;

@Service
@Qualifier("threeDService")
public class MasterCardPaymentService implements IPaymentService {

    @Value("MasterCard")
    private String type;

    public void masterPay(){
        System.out.println("From MasterCardPaymentService " + type);
    }

    public void pay() {
        masterPay();
    }
}

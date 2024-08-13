package miu.edu.application;

import miu.edu.framework.Autowired;
import miu.edu.framework.Qualifier;
import miu.edu.framework.Service;

@Service
public class MasterCardPayment implements IPaymentService {

    @Autowired
    @Qualifier("threeDService")
    IPaymentService threeDService;

    public void pay() {
        threeDService.pay();
    }
}

package miu.edu.application;


import miu.edu.framework.Qualifier;
import miu.edu.framework.Service;

@Service
@Qualifier("threeDService")
public class I3DPrinterService implements IPrinterService {
    public void print3D(){
        System.out.println("From I3DPrinterService");
    }

    public void print() {
        print3D();
    }
}

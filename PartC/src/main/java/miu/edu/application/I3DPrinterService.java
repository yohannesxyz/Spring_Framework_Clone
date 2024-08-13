package miu.edu.application;

import miu.edu.frameWork.Qualifier;
import miu.edu.frameWork.Service;
import miu.edu.frameWork.Value;

@Service
@Qualifier("threeDService")
public class I3DPrinterService implements IPrinterService {

    @Value("3D")
    private String type;

    public void print3D(){
        System.out.println("From I3DPrinterService " + type);
    }

    public void print() {
        print3D();
    }
}

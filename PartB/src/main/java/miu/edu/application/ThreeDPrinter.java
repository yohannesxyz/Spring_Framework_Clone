package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Qualifier;
import miu.edu.framework.Service;

@Service
public class ThreeDPrinter implements IPrinterService {

    @Autowired
    @Qualifier("threeDService")
    IPrinterService threeDService;

    public void print() {
        threeDService.print();
    }
}

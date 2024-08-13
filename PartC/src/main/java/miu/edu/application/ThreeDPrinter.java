package miu.edu.application;

import miu.edu.frameWork.Autowired;
import miu.edu.frameWork.Qualifier;
import miu.edu.frameWork.Service;

@Service
public class ThreeDPrinter implements IPrinterService {

    @Autowired
    @Qualifier("threeDService")
    IPrinterService threeDService;

    public void print() {
        threeDService.print();
    }
}

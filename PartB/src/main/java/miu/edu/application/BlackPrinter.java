package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Service;

@Service
public class BlackPrinter implements IPrinterService {

    public BlackPrinter() {
    }
    IPrinterService printService;
    @Autowired
    public BlackPrinter(IPrinterService printService) {
        this.printService = printService;

    }
    public void print() {
        System.out.println("From BlackPrinter constructor Injection");
    }


}





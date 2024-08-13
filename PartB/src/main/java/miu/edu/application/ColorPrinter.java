package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Service;

@Service

public class ColorPrinter implements IPrinterService {


    IPrinterService printService;


    @Autowired
    public void setPrintService(IPrinterService printService) {
      this.printService = printService;
    }

public void print() {
        System.out.println("From ColorPrinter setter Injection");
    }
}

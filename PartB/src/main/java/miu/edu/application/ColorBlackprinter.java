package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Service;

@Service
public class ColorBlackprinter implements IPrinterService {
    @Autowired
    IPrinterService printService;


    public void print() {
       System.out.println("From ColorBlackprinter Field Injection");
    }
}

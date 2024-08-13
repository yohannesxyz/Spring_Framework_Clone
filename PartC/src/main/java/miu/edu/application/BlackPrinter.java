package miu.edu.application;


import miu.edu.frameWork.Autowired;
import miu.edu.frameWork.Service;
import miu.edu.frameWork.Value;

@Service
public class BlackPrinter implements IPrinterService {

    @Value("black")
    private int color;
    public BlackPrinter() {
    }
    IPrinterService printService;
    @Autowired
    public BlackPrinter(IPrinterService printService) {
        this.printService = printService;

    }
    public void print() {
        System.out.println("From BlackPrinter constructor Injection. This is the color value of "+ color);
    }


}





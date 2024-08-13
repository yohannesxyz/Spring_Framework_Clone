package miu.edu.application;

import miu.edu.frameWork.Autowired;
import miu.edu.frameWork.Service;
import miu.edu.frameWork.Value;

@Service
public class ColorBlackprinter implements IPrinterService {
    @Autowired
    IPrinterService printService;

    @Value("color")
    private String colorType;
    public void print() {
       System.out.println("From ColorBlackprinter Field Injection. And the color type is "+ colorType);
    }
}

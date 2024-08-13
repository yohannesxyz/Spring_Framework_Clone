package miu.edu.application;


import miu.edu.framework.Autowired;
import miu.edu.framework.Service;

@Service
public class Printer {

    @Autowired
    private PrintService printService;

    public void printer() {

        printService.print();
    }
}

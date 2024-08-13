package miu.edu.application;


import miu.edu.frameWork.Service;

@Service
public class PrintService {

    public PrintService() {
    }
    public void print(String message) {
        System.out.println( "PrintService.print() from "+message);
    }
}


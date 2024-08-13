package miu.edu.application;

import miu.edu.framework.FWContext;

public class Main {
    public static void main(String[] args) {
        FWContext context= new FWContext();
        Printer printer=(Printer)context.getServiceBeanOfType(Printer.class);
        printer.printer();
    }
}
package miu.edu.application;


import miu.edu.framework.FWContext;

public class Main {
    public static void main(String[] args) {
        FWContext context = new FWContext();
        context.readServiceClasses();

        BlackPrinter blackPrinter = context.getBean(BlackPrinter.class);
        blackPrinter.print();
        ColorBlackprinter colorBlackprinter = context.getBean(ColorBlackprinter.class);
        colorBlackprinter.print();
        ColorPrinter colorPrinter = context.getBean(ColorPrinter.class);
        colorPrinter.print();
        ThreeDPrinter threeDPrinter = context.getBean(ThreeDPrinter.class);
        threeDPrinter.print();


    }
}
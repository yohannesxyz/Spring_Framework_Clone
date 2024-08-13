package miu.edu.application;


import miu.edu.framework.FWContext;

public class Application implements Runnable {
    @Override
    public void run() {
        try {
            FWContext context = new FWContext();
            context.readServiceClasses();

            PaypalPayment blackPrinter = context.getBean(PaypalPayment.class);
            blackPrinter.pay();

            VisaPaypalPayment visaPaypalPayment = context.getBean(VisaPaypalPayment.class);
            visaPaypalPayment.pay();

            VisaPayment colorPrinter = context.getBean(VisaPayment.class);
            colorPrinter.pay();

            MasterCardPayment threeDPrinter = context.getBean(MasterCardPayment.class);
            threeDPrinter.pay();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.run();

    }
}
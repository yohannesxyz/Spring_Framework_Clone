package org.asd.application;


import org.asd.frameWork.*;

public class Application {


    public static void main(String[] args) {

        FWContext context = new FWContext();
        context.readServiceClasses();

        CustomerService customerService = new CustomerService();


    //    customerService.addCustomer("John", "Doe", "   1234", "Fairfield", "43234");
        context.executeBeforeAndAfterMethods(customerService.addCustomer("John", "Doe", "   1234", "Fairfield", "43234"));

        //Testing logging aspect methods work
        LoggingAspect loggingTest = new LoggingAspect();
        loggingTest.beforeAddCustomerAdvice();
        loggingTest.afterAddCustomerAdvice();
        loggingTest.aroundAddCustomerAdvice();
    }
}





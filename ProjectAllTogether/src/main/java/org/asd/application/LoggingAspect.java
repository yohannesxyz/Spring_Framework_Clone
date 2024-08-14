package org.asd.application;

import org.asd.frameWork.After;
import org.asd.frameWork.Around;
import org.asd.frameWork.Aspect;
import org.asd.frameWork.Before;

@Aspect
class LoggingAspect {
    @Before(value = "execution(public * org.asd.application.CustomerService.addCustomer(..))")
    public void beforeAddCustomerAdvice() {
        System.out.println("Executing @Before advice for addCustomer method.");
    }

    @After(value = "execution(public * org.asd.application.CustomerService.addCustomer(..))")
    public void afterAddCustomerAdvice() {
        System.out.println("Executing @After advice for addCustomer method.");
    }
    @Around(value = "execution(public * org.asd.application.CustomerService.addCustomer(..))")
    public void aroundAddCustomerAdvice() {
        System.out.println("Executing @Around advice before addCustomer method.");
        beforeAddCustomerAdvice();
        System.out.println("Executing @Around advice before addCustomer method.");
    }
}
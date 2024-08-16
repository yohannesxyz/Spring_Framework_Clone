package framework.utils;

import framework.FWContext;

import java.lang.reflect.InvocationTargetException;

public class FinalUtil extends ServiceInjectorUtil {


    public FinalUtil(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {

    }
}

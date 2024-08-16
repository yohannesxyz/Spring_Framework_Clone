package framework.utils;

import framework.FWContext;

import java.lang.reflect.InvocationTargetException;

public abstract class ServiceInjectorUtil {
    protected ServiceInjectorUtil nextHandler;
    protected FWContext fwContext;

    public ServiceInjectorUtil(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        this.nextHandler = nextHandler;
        this.fwContext = fwContext;
    }

    public abstract void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}

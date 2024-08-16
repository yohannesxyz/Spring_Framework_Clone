package framework.utils;

import framework.FWContext;
import framework.annotations.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventListenerUtil extends ServiceInjectorUtil {


    public EventListenerUtil(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Method[] methods = serviceObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(EventListener.class)) {
                Class<?>[] parameters = method.getParameterTypes();
                Class parameterClass = parameters[0];
                fwContext.getEventContext().addEventListeners(parameterClass.getName(),serviceObject, method);
            }
        }
        nextHandler.handle(serviceObject);
    }
}

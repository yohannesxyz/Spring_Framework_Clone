package framework.utils;

import framework.FWContext;
import framework.annotations.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetterInjector extends ServiceInjectorUtil {


    public SetterInjector(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Method[] methods = serviceObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Autowired.class)) {
                Class<?>[] methodParameters = method.getParameterTypes();
                Class<?> parameterType = methodParameters[0];
                Object instance = fwContext.getServiceBeanOftype(parameterType);
                method.invoke(serviceObject, instance);
            }
        }
        nextHandler.handle(serviceObject);
    }
}

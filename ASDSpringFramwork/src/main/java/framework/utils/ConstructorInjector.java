package framework.utils;


import framework.FWContext;
import framework.annotations.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInjector extends ServiceInjectorUtil {


    public ConstructorInjector(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean isNewObject=false;
        Constructor[] constructors = serviceObject.getClass().getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                Class<?>[] methodParameters = constructor.getParameterTypes();
                Class<?> parameterType = methodParameters[0];
                Object parameterInstance = fwContext.getServiceBeanOftype(parameterType);
                Object serviceClassInstance = (Object) constructor.newInstance(parameterInstance);
                fwContext.getServiceObjectMap().put(serviceClassInstance.getClass().getName(), serviceClassInstance);
                isNewObject=true;
                nextHandler.handle(serviceClassInstance);
            }
        }
        if (!isNewObject)
           nextHandler.handle(serviceObject);
    }
}

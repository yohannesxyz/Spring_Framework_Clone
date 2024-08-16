package framework.utils;

import framework.FWContext;
import framework.annotations.Autowired;
import framework.annotations.Qualifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FieldInjector extends ServiceInjectorUtil {


    public FieldInjector(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Field field : serviceObject.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                if (field.isAnnotationPresent(Qualifier.class)) {
                    Annotation annotation = field.getAnnotation(Qualifier.class);
                    String className = ((Qualifier) annotation).name();
                    Object instance = fwContext.getServiceBeanWithName(className);
                    field.setAccessible(true);
                    field.set(serviceObject, instance);
                } else {
                    Class<?> theFieldType = field.getType();
                    Object instance = fwContext.getServiceBeanOftype(theFieldType);
                    field.setAccessible(true);
                    field.set(serviceObject, instance);
                }
            }
        }
        nextHandler.handle(serviceObject);

    }
}

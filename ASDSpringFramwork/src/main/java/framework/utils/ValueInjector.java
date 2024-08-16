package framework.utils;

import framework.ConfigFileReader;
import framework.FWContext;
import framework.annotations.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ValueInjector extends ServiceInjectorUtil {


    public ValueInjector(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Properties properties = ConfigFileReader.getConfigProperties();
        for (Field field : serviceObject.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                Class<?> theFieldType = field.getType();
                if (field.getType().getName().contentEquals("java.lang.String")) {
                    String attrValue = field.getAnnotation(Value.class).name();
                    String toBeInjectedString = properties.getProperty(attrValue);
                    field.setAccessible(true);
                    field.set(serviceObject, toBeInjectedString);
                }
            }
        }
        nextHandler.handle(serviceObject);
    }
}

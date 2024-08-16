package framework;

import framework.annotations.Autowired;

import java.lang.reflect.Field;

public class SpringFramework {
    public static void run(Class applicationClass) {
        FWContext fWContext = new FWContext();
        fWContext.readServiceClasses();
        try {
            Object applicationObject = (Object) applicationClass.getDeclaredConstructor().newInstance();
            for (Field field : applicationObject.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> theFieldType = field.getType();
                    Object instance = fWContext.getServiceBeanOftype(theFieldType);
                    field.setAccessible(true);
                    field.set(applicationObject, instance);
                }
            }
            if (applicationObject instanceof Runnable)
                ((Runnable)applicationObject).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

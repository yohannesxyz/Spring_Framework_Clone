package miu.edu.framework;

import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class FWContext {
    private static List<Object> serviceObjectMap = new ArrayList<>();
    List<Object> serviceObjectInstantiated = new ArrayList<>();
    private static Properties properties;

    public FWContext() {
        loadProperties();
    }

    public void readServiceClasses(){
        try {
            Reflections reflections = new Reflections("miu.edu.application");
            Set<Class<?>> serviceTypes = reflections.getTypesAnnotatedWith(Service.class);


            for (Class<?> serviceClass : serviceTypes) {
                serviceObjectMap.add((Object) serviceClass.getDeclaredConstructor().newInstance());
            }
            performDI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void performDI() {
        try {

            for (Object theClass : serviceObjectMap) {
                Class<?> clazz = theClass.getClass();
                Constructor<?>[] constructors= clazz.getConstructors();
                     for(Constructor<?> constructor:constructors){
                         if(constructor.isAnnotationPresent(Autowired.class)){
                          Class<?>[] parameterTypes=constructor.getParameterTypes();
                          Object[] parameterInstances=new Object[parameterTypes.length];
                          for(int i=0;i<parameterTypes.length;i++){
                              Class<?> parameterType=parameterTypes[i];
                              parameterInstances[i]=getServiceBeanOfType(parameterType);
                          }
                              constructor.setAccessible(true);
                              Object instance=constructor.newInstance(parameterInstances);
                              serviceObjectInstantiated.add(instance);
                              break;
                          }
                     }

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Autowired.class) && method.getName().startsWith("set")) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Object[] parameterInstances = new Object[parameterTypes.length];
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            parameterInstances[i] = getServiceBeanOfType(parameterType);
                        }
                        method.setAccessible(true);
                        method.invoke(theClass, parameterInstances);
                        serviceObjectInstantiated.add(theClass);
                    }
                }
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> fieldType = field.getType();
                        Object fieldInstance;
                        if(field.isAnnotationPresent(Qualifier.class)) {
                            String qualifier = field.getAnnotation(Qualifier.class).value();
                             fieldInstance = getServiceBeanOfQualifier(field.getType(), qualifier);

                        }
                        else {
                             fieldInstance = getServiceBeanOfType(fieldType);
                        }
                        field.setAccessible(true);
                        field.set(theClass, fieldInstance);
                        serviceObjectInstantiated.add(theClass);
                    }
                    if (field.isAnnotationPresent(Value.class)) {
                        Value annotation = field.getAnnotation(Value.class);
                        String key = annotation.value();
                        String value = properties.getProperty(key);
                        if (value != null) {
                            field.setAccessible(true);
                            field.set(theClass, convertValueToFieldType(field.getType(), value));
                        }
                    }
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = FWContext.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Object convertValueToFieldType(Class<?> fieldType, String value) {
        if (fieldType == String.class) {
            return value;
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else {

            return null;
        }
    }


    public Object getServiceBeanOfType(Class interfaceClass) {
        Object service = null;
        try {
            for (Object theClass : serviceObjectMap) {
                if (theClass.getClass().getName().equals(interfaceClass.getName()))
                    return theClass;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }
    public Object getServiceBeanOfQualifier(Class interfaceClass,String qualifier) {
        Object service = null;
        try {
            for (Object theClass : serviceObjectMap) {
                if (interfaceClass.isInstance(theClass)) {
                    Qualifier annotation = theClass.getClass().getAnnotation(Qualifier.class);
                    if (annotation != null && qualifier.equals(annotation.value())) {
                        return theClass;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }
    public <T> T getBean(Class<T> beanClass) {
        for (Object bean : serviceObjectInstantiated) {
            if (beanClass.isInstance(bean)) {
                return beanClass.cast(bean);
            }
        }
        return null;
    }

    public void run(Class<?> applicationClass) {
        try {
            readServiceClasses();
            performDI();

            Object applicationObject = createApplicationInstance(applicationClass);

            if (applicationObject instanceof Runnable) {
                ((Runnable) applicationObject).run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object createApplicationInstance(Class<?> applicationClass) throws Exception {
        Constructor<?> constructor = applicationClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

}

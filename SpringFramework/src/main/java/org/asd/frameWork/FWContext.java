package org.asd.frameWork;



import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class FWContext {
    private static List<Object> serviceObjectMap = new ArrayList<>();

    private static List<Object> aspectObjectMap = new ArrayList<>();

    List<Object> serviceObjectInstantiated = new ArrayList<>();
    List<Object> listenerObjectMap = new ArrayList<>();

    List<Object> beforeObject = new ArrayList<>();
    List<Object> afterObject = new ArrayList<>();

    private static Properties properties;
    private String activeProfile;
    private Timer scheduler;

    public FWContext() {
        loadProperties();


    }

    public void readServiceClasses() {
        try {
            Reflections reflections = new Reflections("org.asd.application");
            Set<Class<?>> serviceTypes = reflections.getTypesAnnotatedWith(Service.class);
            Set<Class<?>> aspectTypes = reflections.getTypesAnnotatedWith(Aspect.class);

            for (Class<?> serviceClass : serviceTypes) {
                Profile profileAnnotation = serviceClass.getAnnotation(Profile.class);
                if (profileAnnotation == null || hasMatchingProfile(profileAnnotation.value())) {
                    serviceObjectMap.add(serviceClass.getDeclaredConstructor().newInstance());
                }
            }
            for(Class<?> aspectClass: aspectTypes){
                aspectObjectMap.add(aspectClass);
            }
//            serviceObjectMap.stream().forEach(serviceObject -> System.out.println(serviceObject.getClass().getName()) );
//            System.out.println("serviceObjectMap.size() = " + serviceObjectMap.size());
            performDI();
            scheduleTasks();
//            listenerObjectMap.stream().forEach(listenerObject -> System.out.println(listenerObject.getClass().getName()) );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void performDI() {
        try {

            for(Object theClass: aspectObjectMap){
                Class<?> clazz = theClass.getClass();
                for(Method method: clazz.getDeclaredMethods()){
                    if(method.isAnnotationPresent(Before.class)){
                        beforeObject.add(theClass);
                    }
                    if(method.isAnnotationPresent(After.class)){
                        afterObject.add(theClass);
                    }
                }
            }

            for (Object theClass : serviceObjectMap) {
                Class<?> clazz = theClass.getClass();
                Constructor<?>[] constructors = clazz.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (constructor.isAnnotationPresent(Autowired.class)) {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        Object[] parameterInstances = new Object[parameterTypes.length];
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            parameterInstances[i] = getServiceBeanOfType(parameterType);
                        }
                        constructor.setAccessible(true);
                        Object instance = constructor.newInstance(parameterInstances);
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
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(EventListener.class)) {
                        listenerObjectMap.add(theClass);
                    }
                }

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> fieldType = field.getType();
                        Object fieldInstance;
                        if (field.isAnnotationPresent(Qualifier.class)) {
                            String qualifier = field.getAnnotation(Qualifier.class).value();
                            fieldInstance = getServiceBeanOfQualifier(field.getType(), qualifier);

                        } else {
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
                activeProfile = properties.getProperty("profiles.active");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasMatchingProfile(String[] profiles) {
        if (activeProfile != null) {
            for (String profile : profiles) {
                if (profile.equals(activeProfile)) {
                    return true;
                }
            }
        }
        return false;
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
            // Add support for other data types as needed
            return null;
        }
    }

    private void scheduleTasks() {
        for (Object theClass : serviceObjectInstantiated) {
            Class<?> clazz = theClass.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Scheduled.class)) {
                    Scheduled scheduledAnnotation = method.getAnnotation(Scheduled.class);
                    long delay = scheduledAnnotation.value();
                    String corn = scheduledAnnotation.cron();

                    if (delay > 0) {
                        scheduler.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    method.setAccessible(true);
                                    method.invoke(theClass);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, delay, delay);
                    } else if (!corn.isEmpty()) {
                        scheduler.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    method.setAccessible(true);
                                    method.invoke(theClass);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, getCronDelay(corn), getCronDelay(corn));
                    }
                }
            }
        }
    }

    private long getCronDelay(String cronExpression) {
        String[] parts = cronExpression.split("\\s+");
        int seconds = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        return (minutes * 60 + seconds) * 1000;
    }

    public void publishEvent(Object event) {
        for (Object listener : listenerObjectMap) {
            Class<?> clazz = listener.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(EventListener.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(event.getClass())) {
                        try {
                            method.setAccessible(true);
                            method.invoke(listener, event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Object getServiceBeanOfType(Class interfaceClass) {
        Object service = null;
        try {
            for (Object theClass : serviceObjectMap) {
                Class<?>[] interfaces = theClass.getClass().getInterfaces();
                for (Class<?> theInterface : interfaces) {
                    if (theInterface.getName().contentEquals(interfaceClass.getName()))
                        return theClass;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

    public Object getServiceBeanOfQualifier(Class interfaceClass, String qualifier) {
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

    public void executeBeforeAndAfterMethods(Object theClass) {
        try {
            Class<?> clazz = theClass.getClass();

            for (Object beforeObj : beforeObject) {
                if (beforeObj == theClass) {
                    Method beforeMethod = findMethodWithAnnotation(beforeObj.getClass(), Before.class);
                    if (beforeMethod != null) {
                        invokeMethodWithDependencies(beforeObj, beforeMethod);
                    }
                }
            }

            // Invoke the test methods here...

            for (Object afterObj : afterObject) {
                if (afterObj == theClass) {
                    Method afterMethod = findMethodWithAnnotation(afterObj.getClass(), After.class);
                    if (afterMethod != null) {
                        invokeMethodWithDependencies(afterObj, afterMethod);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Method findMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }
        return null;
    }

    private void invokeMethodWithDependencies(Object object, Method method) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameterInstances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            parameterInstances[i] = getServiceBeanOfType(parameterType);
        }
        method.setAccessible(true);
        method.invoke(object, parameterInstances);
    }
    public void executeAroundMethods(Object theClass) {
        try {
            Class<?> clazz = theClass.getClass();

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Around.class)) {
                    invokeAroundMethodWithDependencies(theClass, method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void invokeAroundMethodWithDependencies(Object object, Method method) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameterInstances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            parameterInstances[i] = getServiceBeanOfType(parameterType);
        }
    }



    }





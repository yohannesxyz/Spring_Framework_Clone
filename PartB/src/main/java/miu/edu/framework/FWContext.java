package miu.edu.framework;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FWContext {
    private static List<Object> serviceObjectMap = new ArrayList<>();
    List<Object> serviceObjectInstantiated = new ArrayList<>();

    public FWContext() {

    }

    public void readServiceClasses(){
        try {
            Reflections reflections = new Reflections("");
            Set<Class<?>> serviceTypes = reflections.getTypesAnnotatedWith(Service.class);

            for (Class<?> serviceClass : serviceTypes) {
                serviceObjectMap.add((Object) serviceClass.getDeclaredConstructor().newInstance());
            }
//            serviceObjectMap.stream().forEach(serviceObject -> System.out.println(serviceObject.getClass().getName()) );
//            System.out.println("serviceObjectMap.size() = " + serviceObjectMap.size());
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
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getServiceBeanOfType(Class interfaceClass) {
        Object service = null;
        try {
            for (Object theClass : serviceObjectMap) {
                //check class without interface
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
}

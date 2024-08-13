package miu.edu.framework;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FWContext {
    private static List<Object> serviceClassObject=new ArrayList<>();

    public FWContext(){
        try {
            Reflections reflections = new Reflections("");
            Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
            for (Class<?> serviceClass : serviceClasses) {
                serviceClassObject.add((Object) serviceClass.newInstance());
            }

            performDI();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void performDI(){
        try{
            for(Object serviceClass : serviceClassObject){

                for(Field field : serviceClass.getClass().getDeclaredFields()) {
                    if(field.isAnnotationPresent(Autowired.class)){
                        Class<?> theFieldType=field.getType();
                        Object instance=getServiceBeanOfType(theFieldType);
                        field.setAccessible(true);
                        field.set(serviceClass,instance);
                }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Object getServiceBeanOfType(Class instanceClass){
        Object service = null;
        try{
            for(Object theClass: serviceClassObject){
              Class<?> clazz=theClass.getClass();
                  if(clazz.getName().contentEquals(instanceClass.getName())){
                      service=theClass;

                  }
              }

        }catch (Exception e){
            e.printStackTrace();
        }
        return service;
    }
}

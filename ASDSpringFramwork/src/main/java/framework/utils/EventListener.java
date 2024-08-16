package framework.utils;

import java.lang.reflect.Method;

public class EventListener {
    private Object serviceObject;
    private Method listenerMethod;

    public EventListener(Object serviceObject, Method listenerMethod) {
        this.serviceObject = serviceObject;
        this.listenerMethod = listenerMethod;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public Method getListenerMethod() {
        return listenerMethod;
    }
}

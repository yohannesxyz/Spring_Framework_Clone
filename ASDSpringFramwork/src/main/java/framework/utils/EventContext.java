package framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventContext {
    private static Map<String, List<EventListener>> eventListenerMap = new HashMap<>();

    public void addEventListeners(String eventType, Object object, Method method ){
        List<EventListener> eventList = eventListenerMap.get(eventType);
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        eventList.add(new EventListener(object, method));
        eventListenerMap.put(eventType, eventList);
    }

    public void publish(Object eventObject) throws InvocationTargetException, IllegalAccessException {
        List<EventListener> eventList = eventListenerMap.get(eventObject.getClass().getName());
        for (EventListener eventListenerMethod : eventList) {
            eventListenerMethod.getListenerMethod().invoke(eventListenerMethod.getServiceObject(), eventObject);
        }
    }
}


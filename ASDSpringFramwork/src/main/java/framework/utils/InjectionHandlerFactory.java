package framework.utils;

import framework.FWContext;

public class InjectionHandlerFactory {
    public static ServiceInjectorUtil getChainHandler(FWContext fwContext){
        FinalUtil lastHandler = new FinalUtil(null, fwContext);
        EventListenerUtil listenerHandler = new EventListenerUtil(lastHandler, fwContext);
        ScheduledTaskUtil scheduledMethodsHandler = new ScheduledTaskUtil(listenerHandler, fwContext);
        ValueInjector valueInjectionHandler = new ValueInjector(scheduledMethodsHandler, fwContext);
        SetterInjector setterInjectionHandler = new SetterInjector(valueInjectionHandler, fwContext);
        FieldInjector fieldInjectionHandler = new FieldInjector(setterInjectionHandler, fwContext);
        ConstructorInjector constructorInjectionHandler = new ConstructorInjector(fieldInjectionHandler, fwContext);
        return constructorInjectionHandler;

    }
}

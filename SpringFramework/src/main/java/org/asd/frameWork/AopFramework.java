package org.asd.frameWork;

import java.lang.reflect.Method;
import java.util.Map;

public class AopFramework {

        private final Map<Class<?>, Object> objectMap;

        public AopFramework(Map<Class<?>, Object> objectMap) {
            this.objectMap = objectMap;
        }

        public void runTests() throws Exception {
            for (Object theTestClass : objectMap.values()) {
                Method beforeMethod = findBeforeMethod(theTestClass);
                for (Method method : theTestClass.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Aspect.class)) {
                        Object instance = createInstanceWithDependencies(theTestClass);
                        if (beforeMethod != null) {
                            beforeMethod.invoke(instance);
                        }
                        method.invoke(instance);
                    }
                }
            }
        }

        private Method findBeforeMethod(Object theTestClass) {
            for (Method method : theTestClass.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Before.class)) {
                    return method;
                }
            }
            return null;
        }

        private Object createInstanceWithDependencies(Object theTestClass) {
            Class<?> testClass = theTestClass.getClass();
            Object instance = objectMap.get(testClass);
            if (instance == null) {
                throw new RuntimeException("Dependency not found for " + testClass.getSimpleName());
            }
            return instance;
        }
    }



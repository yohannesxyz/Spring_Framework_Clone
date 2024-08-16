package framework.utils;

import framework.FWContext;
import framework.SchedulerTimerTask;
import framework.annotations.Scheduled;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;

public class ScheduledTaskUtil extends ServiceInjectorUtil {


    public ScheduledTaskUtil(ServiceInjectorUtil nextHandler, FWContext fwContext) {
        super(nextHandler, fwContext);
    }

    @Override
    public void handle(Object serviceObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Method[] methods = serviceObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Scheduled.class)) {
                Annotation annotation = method.getAnnotation(Scheduled.class);
                int rate = ((Scheduled) annotation).fixedRate();
                String cron = ((Scheduled) annotation).cron();

                Timer timer = new Timer();
                if (rate > 0)
                    timer.scheduleAtFixedRate(new SchedulerTimerTask(serviceObject, method), 0, rate);

                if (cron != "") {
                    int cronrate = getCronRate(cron);
                    if (cronrate > 0)
                        timer.scheduleAtFixedRate(new SchedulerTimerTask(serviceObject, method), 0, cronrate);
                }
            }
        }
        nextHandler.handle(serviceObject);
    }

    public int getCronRate(String cron) {
        String[] splitresult = cron.split(" ");
        String secondsString = splitresult[0];
        String minutesString = splitresult[1];
        int seconds = Integer.parseInt(secondsString);
        int minutes = Integer.parseInt(minutesString);
        return (minutes * 60 + seconds) *1000;
    }
}

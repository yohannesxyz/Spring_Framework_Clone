package application;

import framework.SpringFramework;
import framework.annotations.Autowired;

import java.lang.reflect.InvocationTargetException;

public class Application implements Runnable {
    @Autowired
    private TaskService taskService;

    public static void main(String[] args) {
        SpringFramework.run(Application.class);
    }

    @Override
    public void run() {
        try {
            taskService.completeTask();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

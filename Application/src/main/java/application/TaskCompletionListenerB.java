package application;

import framework.annotations.EventListener;
import framework.annotations.Service;

@Service
public class TaskCompletionListenerB {
    @EventListener
    public void listen(TaskCompletionEvent taskCompletionEvent) {
        System.out.println("TaskCompletionListenerB: " + taskCompletionEvent);
    }
}

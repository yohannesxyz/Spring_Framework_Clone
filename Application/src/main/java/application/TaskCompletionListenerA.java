package application;

import framework.annotations.EventListener;
import framework.annotations.Service;

@Service
public class TaskCompletionListenerA {
    @EventListener
    public void listen(TaskCompletionEvent taskCompletionEvent) {
        System.out.println("TaskCompletionListenerA: " + taskCompletionEvent);
    }
}

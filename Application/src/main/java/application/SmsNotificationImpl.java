package application;

import framework.annotations.Async;
import framework.annotations.Service;

@Service
public class SmsNotificationImpl implements SmsNotification {
    @Async
    public void sendSMS(String content) {
        System.out.println("sending sms: " + content);
    }
}

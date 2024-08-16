package application;

import framework.annotations.Service;

@Service
public class PushNotification {
    public void sendPushMessage(String content) {
        System.out.println("sending push notification: " + content);
    }
}

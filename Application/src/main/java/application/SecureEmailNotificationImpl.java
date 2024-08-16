package application;

import framework.annotations.Profile;
import framework.annotations.Service;
import framework.annotations.Value;

@Service
@Profile(value="prod")
public class SecureEmailNotificationImpl implements EmailNotification {
    @Value(name="emailMessage")
    String emailMessage;

    public void sendEmail(String content) {
        System.out.println("sending secure email: " + content + " , message=" + emailMessage);
    }
}

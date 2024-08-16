package application;

import framework.annotations.Profile;
import framework.annotations.Service;
import framework.annotations.Value;

@Service
@Profile(value="dev")
public class EmailNotificationImpl implements EmailNotification {
    @Value(name="emailMessage")
    String emailMessage;

    public void sendEmail(String content) {
        System.out.println("sending email: " + content + " , message=" + emailMessage);
    }
}

package application;

import framework.annotations.*;
import framework.utils.EventPublisher;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class TaskService {

    @Autowired
    @Qualifier(name="application.Logger")
    private Logger logger;

    @Autowired
    private EmailNotification emailNotification;

    private SmsNotification smsNotification;
    private PushNotification pushNotification;

    @Autowired
    EventPublisher publisher;

    @Value(name="companyname")
    String companyName;

    public TaskService() {
    }

    @Autowired
    public TaskService(PushNotification pushNotification) {
        this.pushNotification = pushNotification;
    }

    public void setEmailNotification(EmailNotification emailNotification) {
        this.emailNotification = emailNotification;
    }

    @Autowired
    public void setSmsNotification(SmsNotification smsNotification) {
        this.smsNotification = smsNotification;
    }

    public void completeTask() throws InvocationTargetException, IllegalAccessException {
        emailNotification.sendEmail(companyName + " - Task completed successfully");
        smsNotification.sendSMS("Task completed successfully");
        pushNotification.sendPushMessage("Task completed successfully");
        logger.log("Task completed");
        publisher.publish(new TaskCompletionEvent());
    }

    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void processPendingTasks() {
        Date date = Calendar.getInstance().getTime();
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        String currentTime = timeFormatter.format(date);
        System.out.println("Processing pending tasks at " + currentTime);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void generateDailyTaskReport() {
        Date date = Calendar.getInstance().getTime();
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        String currentTime = timeFormatter.format(date);
        System.out.println("Generating daily task report at " + currentTime);
    }
}

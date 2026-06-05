public class SmsNotification implements NotificationService{ //connects to the NotificationService
    @Override
    public void sendNotification(String message){
        System.out.println("[SMS NOTIFICATION]: " + message);
    }
}


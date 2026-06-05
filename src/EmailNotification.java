public class EmailNotification implements NotificationService{ //connects to the NotificationService
    @Override
    public void sendNotification(String message) {
        System.out.println("[EMAIL NOTIFICATION]: " + message);
    }
}

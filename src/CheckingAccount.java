import java.time.LocalDate;

public class CheckingAccount extends Account {
    private double dailyLimit;
    private double totalAmount = 0;
    private LocalDate lastWithdrawDate;

    public CheckingAccount(String accountNo, String customerName, double dailyLimit, NotificationService notificationService){ // constructor of checkingAccount
        super(accountNo, customerName, notificationService); // calls Account(main class) to sent main's constructor
        this.dailyLimit = dailyLimit;
    }

    @Override
    public void withdraw(double amount,LocalDate transactionDate) {
        LocalDate today = transactionDate;
                if (!today.equals(lastWithdrawDate)) { // today's date isn't equal to lastWithdrawDate (next day)
                    totalAmount = 0;//resets total daily withdraws
                    lastWithdrawDate = today;
                }

                if (dailyLimit < amount + totalAmount) {// adds today's all withdraws and compares with daily limit
                    System.out.println("[ERROR]: The transaction could not be completed. Daily withdrawal limit was exceeded.");
                } else if (super.subtractBalance(amount)) {
                    System.out.println("[INFO]: The withdraw transaction completed.");
                    totalAmount += amount;
                    getNotificationService().sendNotification("Amount of " + amount + " TL has been withdrawn from account " + getAccountNo() + ". Current balance: " + getBalance() + " TL");
                }
    }
}

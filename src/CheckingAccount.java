import java.time.LocalDate;

public class CheckingAccount extends Account {
    private double dailyLimit;
    private double totalAmount = 0;
    private LocalDate lastWithdrawDate;

    public CheckingAccount(String accountNo, String customerName, double dailyLimit){ // constructor of checkingAccount
        super(accountNo, customerName); // calls Account(main class) to sent main's constructor
        this.dailyLimit = dailyLimit;
    }

    @Override
    public void withdraw(double amount) {
        LocalDate today = LocalDate.now();
        if(amount > 0) {
            if (amount <= this.balance) {

                if (!today.equals(lastWithdrawDate)) { // today's date isn't equal to lastWithdrawDate (next day)
                    totalAmount = 0;//resets total daily withdraws
                    lastWithdrawDate = today;
                }
                if (dailyLimit < amount + totalAmount) {// adds today's all withdraws and compares with daily limit
                    System.out.println("[ERROR]: The transaction could not be completed. Daily withdrawal limit was exceeded.");
                } else {
                    totalAmount += amount;
                    this.balance -= amount;
                }
            } else {
                System.out.println("[ERROR]: The transaction could not be completed. Insufficient balance.");
            }
        } else {
            System.out.println("[ERROR]: Withdrawal amount must be higher than 0.");
        }
    }
}

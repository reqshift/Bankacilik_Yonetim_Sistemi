public class CheckingAccount extends Account {
    private double dailyLimit;

    public CheckingAccount(String accountNo, String customerName, double dailyLimit){ // constructor of checkingAccount
        super(accountNo, customerName); // calls Account(main class) to sent main's constructor
        this.dailyLimit = dailyLimit;
    }

    @Override
    public void withdraw(double amount) {
        if(dailyLimit < amount){
            System.out.println("[ERROR]: The transaction could not be completed. Insufficient balance or the daily withdrawal limit was exceeded.");
        } else {
            super.withdraw(amount);
        }
    }
}

import java.time.LocalDate;

public class SavingsAccount extends Account{
    private double interestRate; // for interest ratios
    private int termDays; // how many days will be in this account
    private LocalDate interestEndDate; // in that day money can get with interest
    LocalDate today = LocalDate.now();

    public SavingsAccount(String accountNo, String customerName, double interestRate, int termDays){ //constructor of savingsAccount
        super(accountNo, customerName);
        this.interestRate = interestRate;
        this.termDays = termDays;
        //interestEndDate doesn't get from user, it is calculating with today and termDays
        this.interestEndDate = today.plusDays(termDays);
    }
    @Override
    public void deposit(double amount){
        super.deposit(amount);//depositing money after calling deposit method
        this.interestEndDate = today.plusDays(this.termDays);//gets today's day and add termDays again(resets because money deposited
        System.out.println("[INFO]: With your new balance due date: " + this.interestEndDate);//shows new last day
    }

    @Override
    public void withdraw(double amount){
        if(amount > 0) {
                if (today.isBefore(interestEndDate)) {//today isn't end day yet
                    if (amount <= this.balance) {
                        System.out.println("[ERROR]: A withdrawal was made before the due date. Your current interest accrual entitlement has been cancelled.");
                        this.interestEndDate = today.plusDays(this.termDays);//resets end date to now
                        this.balance -= amount;
                    } else {
                        System.out.println("[ERROR]: The transaction could not be completed. Insufficient balance.");
                    }
                } else {
                    double interestIncome = getBalance() * interestRate;
                    this.balance += interestIncome;// interest add to the total balance
                    System.out.println("[SUCCESS]: The due date has been reached! Interest income added: " + interestIncome);

                        if (amount <= this.balance) {
                            this.interestEndDate = today.plusDays(this.termDays);
                            this.balance -= amount;
                        } else {
                            this.interestEndDate = today.plusDays(this.termDays);
                            System.out.println("[ERROR]: The transaction could not be completed. Insufficient balance.");
                        }
                }
        } else {
            System.out.println("[ERROR]: Withdrawal amount must be higher than 0.");
        }
    }
}

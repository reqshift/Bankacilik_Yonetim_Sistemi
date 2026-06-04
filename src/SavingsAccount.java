import java.time.LocalDate;

public class SavingsAccount extends Account{
    private double interestRate; // for interest ratios
    private int termDays; // how many days will be in this account
    private LocalDate interestEndDate; // in that day money can get with interest


    public SavingsAccount(String accountNo, String customerName, double interestRate, int termDays, LocalDate transactionDate){ //constructor of savingsAccount
        super(accountNo, customerName);
        this.interestRate = interestRate;
        this.termDays = termDays;
        //interestEndDate doesn't get from user, it is calculating with today and termDays
        this.interestEndDate = transactionDate.plusDays(termDays);
    }
    @Override
    public boolean deposit(double amount,LocalDate transactionDate){
        LocalDate today = transactionDate;
        if(super.deposit(amount,transactionDate)) {//depositing money after calling deposit method
            this.interestEndDate = today.plusDays(this.termDays);//gets today's day and add termDays again(resets because money deposited
            System.out.println("[INFO]: With your new balance due date: " + this.interestEndDate);//shows new last day
            return true;
        } else {
            System.out.println("[ERROR]: Deposit transaction couldn't perform");
            return false;
        }

    }

    @Override
    public void withdraw(double amount,LocalDate transactionDate){
        LocalDate today = transactionDate;
                if (today.isBefore(interestEndDate)) {//today isn't end day yet
                        System.out.println("[ERROR]: A withdrawal was made before the due date. Your current interest accrual entitlement has been cancelled.");
                    if (super.subtractBalance(amount)) {
                        this.interestEndDate = today.plusDays(this.termDays);//resets end date to now
                    }
                } else {
                    checkAndApplyInterest(transactionDate);
                    if (super.subtractBalance(amount)) {
                        System.out.println("[INFO]: The withdraw transaction completed.");
                        this.interestEndDate = today.plusDays(this.termDays);
                    }
                }
    }

    private boolean checkAndApplyInterest(LocalDate transactionDate) {
        double interestIncome = getBalance() * interestRate;
        if (super.addBalance(interestIncome)) { //interest add to the total balance
            System.out.println("[SUCCESS]:The due date has been reached !Interest income added:" + interestIncome);

            return true;
        } else {
            System.out.println("[ERROR]:Interest income didn't add:");
            return false;
        }

    }

}

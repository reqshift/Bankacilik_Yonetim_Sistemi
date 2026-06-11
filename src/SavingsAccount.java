import java.time.LocalDate;

public class SavingsAccount extends Account{
    private double interestRate; // for interest ratios
    private int termDays; // how many days will be in this account
    private LocalDate interestEndDate; // in that day money can get with interest


    public SavingsAccount(String accountNo, String customerName, double interestRate, int termDays, LocalDate transactionDate, NotificationService notificationService){ //constructor of savingsAccount
        super(accountNo, customerName, notificationService);
        this.interestRate = interestRate;
        this.termDays = termDays;
        //interestEndDate doesn't get from user, it is calculating with today and termDays
        this.interestEndDate = transactionDate.plusDays(termDays);
    }
    public double getInterestRate(){
        return interestRate;
    }
    public LocalDate getInterestEndDate() {
        return this.interestEndDate;
    }
    public boolean deposit(double amount,LocalDate transactionDate){ //override canceled because transactionDate didn't use when we override from account
        LocalDate today = transactionDate;
        if(super.deposit(amount)) {//depositing money after calling deposit method
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
                    if (super.subtractBalance(amount)) {
                        System.out.println("[INFO]: Early withdrawal processed. Your current interest accrual entitlement has been cancelled.");
                        this.interestEndDate = today.plusDays(this.termDays);//resets end date to now
                        getNotificationService().sendNotification(String.format("Amount of %.1f TL has been withdrawn from account %s. Remaining balance: %.2f TL", amount, getAccountNo(), getBalance()));
                    }
                } else {
                    double interestIncome = calculateInterest();
                    double interestTotalBalance = getBalance() + interestIncome; // abstract total balance (not added yet)
                    if (amount <=interestTotalBalance) { //if customer has enough money, interest will add and can be withdrawn
                        if(applyInterest(interestIncome)) {
                            super.subtractBalance(amount);
                            this.interestEndDate = today.plusDays(this.termDays);
                            getNotificationService().sendNotification(String.format("Amount of %.1f TL has been withdrawn from account %s. Remaining balance: %.2f TL", amount, getAccountNo(), getBalance()));
                        }
                    } else {
                        System.out.println("[ERROR]: Your total balance, including interest, is insufficient.");
                    }
                }
    }

    public double calculateInterest() {
        double interestIncome = getBalance() * interestRate;
        return interestIncome;
    }

    public boolean applyInterest(double interestIncome) {
        if (super.addBalance(interestIncome)) { //interest add to the total balance
            System.out.println("[SUCCESS]:The due date has been reached !Interest income added:" + interestIncome);
            return true;
        } else {
            System.out.println("[ERROR]:Interest income didn't add:");
            return false;
        }
    }

}

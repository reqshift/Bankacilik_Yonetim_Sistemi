public class SavingsAccount extends Account{
    private boolean isInterestActive = true;
    private double interestRate; // for interest ratios

    public SavingsAccount(String accountNo, String customerName, double interestRate){
        super(accountNo, customerName);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount){
        if(isInterestActive) { // if it is true
            System.out.println("[ERROR]: A withdrawal was made before the due date. Your current interest accrual entitlement has been cancelled.");
            isInterestActive = false;
        }
        super.withdraw(amount);// accesses to Account's withdraw method
    }
}

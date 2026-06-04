import java.time.LocalDate;

public abstract class Account {
    private String accountNo;
    private String customerName;
    private double balance;

    public Account(String accountNo, String customerName){ //constructor used to access first parameters
        this.accountNo = accountNo; // this. added to prevent duplication ( this means main variables )
        this.customerName = customerName;
        this.balance = 0.0;
    }
    public String getAccountNo(){ //Getter method returns
        return accountNo;
    }
    public void setAccountNo(String accountNo){ //Setter method updates ( doesn't return anything )
        this.accountNo = accountNo;
    }
    public String getCustomerName(){
        return customerName;
    }
    public void setCustomerName(String customerName){
        this.customerName = customerName; //same with constructor body
    }
    public double getBalance(){ //for balance there isn't set because it can't update from set
        return balance;
    }
    //methods
    public boolean deposit(double amount, LocalDate transactionDate) { // deposit method
        if (amount > 0) { // if money is being deposited, add to balance
            this.balance += amount;
            return true;
        } else {
            System.out.println("[ERROR]: Depositing amount must be higher than 0");
            return false;
        }
    }
    public abstract void withdraw(double amount,LocalDate transactionDate);//withdraw method must be abstract because it hasn't common rules

//Balance methods
    protected boolean addBalance(double amount){
        if(amount > 0) {
            this.balance += amount;
            return true;
        } else {
            System.out.println("[ERROR]: Deposited amount must be higher than 0.");
            return false;
        }
    }

    protected boolean subtractBalance(double amount){
        if(amount > 0) {
            if (amount <= this.balance) {
                this.balance -= amount;
                return true;
            } else {
                System.out.println("[ERROR]: The transaction could not be completed. Insufficient balance.");
                return false;
            }
        }else {
            System.out.println("[ERROR]: Withdrawal amount must be higher than 0.");
            return false;
        }
    }



}

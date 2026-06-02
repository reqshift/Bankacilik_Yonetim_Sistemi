public class Account {
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
    public void deposit(double amount) { // deposit method
        if (amount > 0) { // if money is being deposited, add to balance
            this.balance += amount;
        } else {
            System.out.println("[ERROR]: Depositing amount must be higher than 0");
        }
    }
    public void withdraw(double amount){ //withdraw method
        if(amount <= this.balance){// if there isn't enough money in balance, you can't do the transaction
            this.balance -= amount;
        } else {
            System.out.println("[ERROR]: The transaction could not be completed. Insufficient balance.");
        }
    }



}

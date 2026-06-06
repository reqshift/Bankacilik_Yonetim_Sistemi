import java.util.HashMap;
import java.util.Map;
public class Bank {
    private Map<String , Account> accounts; //map uses data types when identifies(accountNo = String(KEY), (VAKUE) = Account)

    public Bank(){ // constructor
        this.accounts = new HashMap<>(); //opens in memory
    }

    public void addAcount(Account account){
        String accountNumber = account.getAccountNo();//accountNo(key) gets from account class
        accounts.put(accountNumber, account); // key=accountNumber, value=account add to the map(.put)
    }
    public Account findAccount(String accountNo){
        return accounts.get(accountNo); //accountNo found and gets
    }
    public double totalBankBalance(){
        double totalBalance = 0.0;
        for(Account account : accounts.values()) {
            // Iterates through all account objects in the map one by one.
            // In each turn, 'account' represents the current account, allowing us to get its balance.
            totalBalance += account.getBalance();
        }
        return totalBalance;

    }
}

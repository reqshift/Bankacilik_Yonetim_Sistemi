import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
public class Bank {
    private Map<String , Account> accounts; //map uses data types when identifies(accountNo = String(KEY), (VAKUE) = Account)

    public Bank(){ // constructor
        this.accounts = new HashMap<>(); //opens in memory
    }
    public Collection<Account> getAllAccounts(){ /// for getting all account lists
        return accounts.values();
    }

    public void addAccount(Account account){
        String accountNumber = account.getAccountNo();//accountNo(key) gets from account class
        if(findAccount(accountNumber) == null){
            accounts.put(accountNumber, account); // key=accountNumber, value=account add to the map(.put)
            if(account instanceof SavingsAccount){
                System.out.println("[SİSTEM]: Vadeli hesap başarıyla sisteme eklendi. Başlangıç bakiyesi: 0.0 TL");
            } else {
                System.out.println("[SİSTEM]: Vadesiz hesap başarıyla sisteme eklendi. Başlangıç bakiyesi: 0.0 TL");
            }
        } else {
            System.out.println("[ERROR]: Already " + accountNumber + " has created.");
        }
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

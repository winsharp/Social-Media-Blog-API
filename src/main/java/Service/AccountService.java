package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    AccountDAO accountDAO; 

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return this.accountDAO.getAll();
    }

    public Account register(Account account){
        //make sure the account doesn't exist already
        //make sure pass is at least 4 characters
        return this.accountDAO.insert(account);
      
     }

     public boolean login (Account account){

        if (this.accountDAO.validateLogin(account.getUsername(), account.getPassword()) != null) {
            return true;
        }
        return false;
     }
    
}

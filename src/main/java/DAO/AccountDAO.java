package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO implements DAOInterface<Account> {

    public List<Account> getAll(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * from account";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }
    
    public Account getByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account WHERE account_id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            //write ps's setString and setInt methods here.

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Account insert(Account account){
        Connection connection = ConnectionUtil.getConnection();
            if (this.userExistCheck(account.getUsername()) != false){
                //write some bs about how it exists already
                return null;
            }
            else if (account.getUsername().replaceAll(" ", "").length() < 1){
                //write some bs bout how the pass isn't long enough
                return null;
            }
            else if (account.getPassword().length() < 4){
                //write some bs bout how the pass isn't long enough
                return null;
            }

        try {
            //Write SQL logic here. When inserting, you only need to define the departure_city and arrival_city
            //values (two columns total!)
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write ps's setString and setInt methods here.
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account validateLogin(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    Account account = new Account(
                            rs.getInt("account_id"),
                            rs.getString("username"),
                            rs.getString("password"));
                
                if (Objects.equals (username, account.getUsername()) && Objects.equals (password, account.getPassword()) && account.getAccount_id() == 1){
                    return account;
                }
            }
        }
        catch(SQLException e){
        }
        return null;
    }

    public boolean userExistCheck(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";
        Connection conn = ConnectionUtil.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // public void update(Account account){
    //     Connection connection = ConnectionUtil.getConnection();
    //     try {
    //         //Write SQL logic here
    //         String sql = "UPDATE account SET departure_city = ?, arrival_city = ? WHERE account_id = ?";
    //         PreparedStatement ps = connection.prepareStatement(sql);

    //         //write PreparedStatement setString and setInt methods here.
    //         ps.setString(1, account.getDeparture_city());
    //         ps.setString(2, account.getArrival_city());
    //         ps.setInt(3, id);

    //         ps.executeUpdate();
    //     }catch(SQLException e){
    //         System.out.println(e.getMessage());
    //     }
    // }

}


    


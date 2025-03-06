package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import Util.ConnectionUtil;
import Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO implements DAOInterface<Message>{

    public List<Message> getAll(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * from message";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getAllByAccountID(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * from message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    public Message getByID(int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Message insert(Message message){
        Connection connection = ConnectionUtil.getConnection();
            if (!this.userExistCheck(message.getPosted_by())){
                return null; //fails if the account doesn't exist
            }
            else if (message.getMessage_text().trim().isEmpty()){
                return null; //fails if the message is empty
            }
            else if (message.getMessage_text().length() > 255){
                return null; //fails if the message length is greater than 255 char limit
            }

        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean update(Message message){       
        if (this.getByID(message.getMessage_id()) == null){
            return false; //fails if the message doesn't exist
        }
        else if (message.getMessage_text().trim().isEmpty()){
            return false; //fails if the new message is empty 
        }
        else if (message.getMessage_text().length() > 255){
            return false; //fails if the message length is greater than 255 char limit
        }

        int rowsUpdated = 0;
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());
            rowsUpdated = ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return rowsUpdated > 0;
    }

    public boolean delete(Message message){
        int rowsUpdated = 0;
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getMessage_id());
            rowsUpdated = ps.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rowsUpdated > 0;
    }

    public boolean userExistCheck(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT 1 FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql); 
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            } 
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
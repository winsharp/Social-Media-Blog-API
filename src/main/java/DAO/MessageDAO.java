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
import java.util.Objects;

public class MessageDAO implements DAOInterface<Message>{

    public List<Message> getAll(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
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
            //Write SQL logic here
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
            //Write SQL logic here
            String sql = "SELECT * FROM message WHERE message_id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            //write ps's setString and setInt methods here.

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
            if (message.getPosted_by() < -1 ){
                //write some bs about how the user doesn't exist
                return null;
            }
            else if (message.getMessage_text().replaceAll(" ", "").length() <=0){
                //write some bs bout how the text is blank
                return null;
            }
            else if (message.getMessage_text().length() >= 255){
                //write some bs bout how the text length is too long
                return null;
            }

        try {
            //Write SQL logic here. When inserting, you only need to define the departure_city and arrival_city
            //values (two columns total!)
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write ps's setString and setInt methods here.
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
        if (this.messageExistCheck(0) == false){
            //write some bs about how the user doesn't exist
            return false;
        }
        else if (message.getMessage_text().replaceAll(" ", "").length() < 1){
            //write some bs bout how the text is blank
            return false;
        }
        else if (message.getMessage_text().length() <= 255){
            //write some bs bout how the text length is too long
            return false;
        }

        Connection connection = ConnectionUtil.getConnection();
        try {
        //Write SQL logic here
        String sql = "UPDATE message SET message_text WHERE message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        //write PreparedStatement setString and setInt methods here.
        ps.setString(1, message.getMessage_text());
        ps.setInt(2, message.getMessage_id());

        ps.executeUpdate();
        }
        catch(SQLException e){
        System.out.println(e.getMessage());

        }
        return false;
    }

    public boolean delete(Message message){
        // if (this.messageExistCheck(message.getMessage_id()) == false){
        //     return true;
        // }

        String sql = "DELETE FROM message WHERE message_id = ?";
        int rowsUpdated = 0;
        Connection conn = ConnectionUtil.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, message.getMessage_id());
            rowsUpdated = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rowsUpdated > 0;
    }

    public boolean messageExistCheck(int id) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        Connection conn = ConnectionUtil.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

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
}

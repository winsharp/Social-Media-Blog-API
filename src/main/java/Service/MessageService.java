package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;


public class MessageService {
    MessageDAO messageDAO; 

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return this.messageDAO.getAll();
    }

    public List<Message> getAllMessagesByAccountId(int id) {
        return this.messageDAO.getAllByAccountID(id);
    }

    public Message getByMessageId(int id) {
        return this.messageDAO.getByID(id);

    }

    public Message create(Message message){
        return this.messageDAO.insert(message);
      
     }

     public boolean update(Message message){
        return this.messageDAO.update(message);
      
     }

     public boolean delete(Message message){
        return this.messageDAO.delete(message);
      
     }
    
}

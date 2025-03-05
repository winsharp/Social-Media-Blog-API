package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService;
    public MessageService messageService;

    public SocialMediaController() {
        // Initialize the accountService and messageService instances
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);

        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getByMessageId);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountId);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
    
   
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerAccount(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.register(account);

        if (registeredAccount == null){
            ctx.status(400);
        }
        else { 
            ctx.json(registeredAccount);
            ctx.status(200);
        }
    }

    private void loginAccount(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
    
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount != null){
            ctx.status(200);
            ctx.json(loggedInAccount);
        }
        else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.create(message);
        if (newMessage == null){
            ctx.status(400);
        }
        else { 
            ctx.json(newMessage);
            ctx.status(200);
        }
    }

    private void getAllMessages(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getByMessageId(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getByMessageId(id);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
            ctx.result("");
        }
    }

    private void getAllMessagesByAccountId(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        ctx.json(messages);
    }

    private void deleteMessageById(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getByMessageId(id);
        if (message != null) {
            messageService.delete(message);
            ctx.json(message);
        } else {
            ctx.status(200);
            ctx.result("");
        }
    }

    private void updateMessageById(Context ctx) throws JsonProcessingException{
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message existingMessage = messageService.getByMessageId(id);
            if (existingMessage == null) {
                ctx.status(400);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            Message updateInfo = mapper.readValue(ctx.body(), Message.class);
            String newMessageText = updateInfo.getMessage_text();
            
            if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
                ctx.status(400);
                return;
            }

            existingMessage.setMessage_text(newMessageText);
            boolean updated = messageService.update(existingMessage);
            if (updated) {
                ctx.json(existingMessage);
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
}
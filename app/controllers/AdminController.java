package controllers;

import play.mvc.*;
import views.html.*;
import play.db.*;
import play.api.libs.json.*;

import play.data.FormFactory;
import javax.inject.Inject;
import play.data.DynamicForm;
import java.util.*;
import services.Place;
import services.User;
import services.BankData;
import services.Queue;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdminController extends Controller { 

    @Inject FormFactory formFactory;

    public Result getUser() throws SQLException {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        if ("SignUp".equals(requestData.get("action"))) 
            return saveUser(requestData);
        else 
            if ("Login".equals(requestData.get("action"))) 
                return authenticate(requestData);
   
        return badRequest("This action is not allowed");
    }

    public Result saveUser(DynamicForm requestData) throws SQLException {
        String email = requestData.get("email");
        String password = requestData.get("password");

        if (User.insertToDatabase(email, password))
            return redirect("/");
        
       return ok(index.render("Obavite sve i ušedite vrijeme", "Registracija nije uspjela!",  formFactory.form(User.class)));
    }

    private Result authenticate(DynamicForm requestData) throws SQLException {
        
        String email = requestData.get("email");
        String password = requestData.get("password");
        
        if (User.existsInDatabase(email, password)) {
            session("connected", email);
            return redirect("/admin");
        }
        
        return ok(index.render("Obavite sve i ušedite vrijeme", "Unešeni su krivi korisnički podaci!",  formFactory.form(User.class)));
    }

    public Result logout() {
        session().remove("connected");
        return redirect("/");
    }

    public Result admin() throws SQLException {
        String user = session("connected");
        if (user != null) {     
            List<BankData> result = BankData.getAllBanksWithData();     
            return ok(freeNumber.render(user, "Oslobađanje broja", result));
        } 

        return redirect("/");  
    }

    public Result viewAll() throws SQLException {
        String user = session("connected");
        if (user != null) {
            List<BankData> result = BankData.getAllBanksWithData();    
            return ok(viewAll.render(user, "Oslobađanje broja", result));
        } 

        return redirect("/");
    }

    public Result freeNumber(String service, int id, int number) throws SQLException {

        Queue.update(id, service, number);
        BankData item = BankData.getBankWithData(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";

        try {
            result = objectMapper.writeValueAsString(item);
        }
        catch(JsonProcessingException e){
            System.out.println(e.toString());
        }

        return ok(result);
    }

}

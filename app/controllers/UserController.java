package controllers;

import play.mvc.*;
import views.html.*;
import play.db.*;
import play.api.libs.json.*;
import java.sql.DriverManager;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import services.Place;
import services.BankData;
import services.Queue;
import java.sql.SQLException;


public class UserController extends Controller { 
    
    public Result map() throws SQLException {
        List<Place> places = Place.getBankPlaces();
        return ok(map.render("Odaberite lokaciju", places));
    }

    public Result chooseService(int id) throws SQLException {
        List<BankData> result = BankData.getAllBanksWithData();        
        return ok(chooseService.render(id, result));
    }

    public Result getNumber(String service, int id, int activeNum, int activeId) throws SQLException {
        int number = 0;

        if (activeId != 0)
        {
            number = Queue.inProcess(activeId, service);          
            if (number != 0 && activeNum >= number){
                System.out.println("Already have a number that has not passed.");
                return ok("/numberError/" + Integer.toString(id) + "/" + Integer.toString(activeNum) + "/" + service);
            }
        }      

        System.out.println("Get a new number.");   
        number = Queue.entry(id, service);

        if (number > 0)
            return ok("/showNumber/" + Integer.toString(id) + "/" + Integer.toString(number) + "/" + service);
        else
            return ok("/numberError/" + service);

    }

    public Result showNumber(int id, int number, String service){
        return ok(showNumber.render(id, number, service));
    }

    public Result numberError(String service){
        return ok(numberError.render(service));
    }
    
    public Result active() throws SQLException {
        List<BankData> result = BankData.getAllBanksWithData();
        return ok(showActiveNumbers.render("Pregled dodijeljenih brojeva", result));
    }

    public Result queue() throws SQLException {
        List<BankData> result = BankData.getAllBanksWithData();
        return ok(queue.render("Pregled poslovnica", result));
    }

}
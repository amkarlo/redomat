package controllers;

import play.mvc.*;
import views.html.*;
import play.db.*;
import play.api.libs.json.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import play.data.FormFactory;
import javax.inject.Inject;
import play.data.DynamicForm;
import java.sql.ResultSet;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import services.Place;
import services.User;
import services.BankData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HomeController extends Controller { 

    @Inject FormFactory formFactory;  

    public Result index() {
        return ok(index.render("Obavite sve i ušedite vrijeme", "",  formFactory.form(User.class)));
    }

}
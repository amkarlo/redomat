package services;

import java.util.*;
import java.sql.ResultSet; 
import java.sql.SQLException;


public class Place {
    private int id;
    private float lat;
    private float lng;
    private String name;
    private String address;

    public Place(int id, float lat, float lng, String name, String address){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.address = address;
    }

    public static List<Place> getBankPlaces() throws SQLException {
        List<Place> result = new ArrayList<Place>();

        ResultSet rs = DatabaseService.executeQuery("SELECT * FROM Banks ORDER BY id");
        while (rs != null && rs.next()) {
            Place item = new Place(rs.getInt("id"), rs.getFloat("lat"), rs.getFloat("lng"), rs.getString("name"), rs.getString("address"));
            result.add(item);
        }

        return result;
    }

}
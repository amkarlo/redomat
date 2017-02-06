package services;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class Queue {
    private String service;
    private List<Integer> details;

    public Queue(String service, List<Integer> details)  {
        this.service = service;
        this.details = details;
    }

    public String getService(){
        return this.service;
    }
    
    public List<Integer> getDetails(){
        return this.details;
    }

    public static int getServiceId(String serviceName) throws SQLException {
        int serviceId = 0;
        ResultSet rs = DatabaseService.executeQuery("SELECT id FROM Services WHERE name LIKE ?", serviceName);
        if (rs != null && rs.next()) {
            serviceId = rs.getInt("id");
        }
        return serviceId;
    }

    public static int inProcess(int id, String service) throws SQLException {
        int ordinal = 0;
        ResultSet rs = DatabaseService.executeQuery("SELECT T.ordinal FROM Transactions T " +
                    "LEFT JOIN Services s ON t.service_id = s.id WHERE " +
                    "T.bank_id='"+ Integer.toString(id) +"' AND S.name LIKE '"+ service +"' " +
                    "AND T.finish IS NULL AND T.process IS NOT NULL");
        if (rs != null && rs.next()){
            ordinal = rs.getInt("ordinal");
        }
        return ordinal;
    }

    public static int lastOrdinal(int id, String service) throws SQLException {
        int ordinal = 0;
        ResultSet rs = DatabaseService.executeQuery("SELECT T.ordinal FROM Transactions T " +
                    "LEFT JOIN Services s ON t.service_id = s.id WHERE " +
                    "T.bank_id=? AND S.name LIKE ? ORDER BY entry DESC LIMIT 1", id, service);
        if (rs != null && rs.next()){
            ordinal = rs.getInt("ordinal");
        }
        return ordinal;
    }

    public static void update(int id, String service, int number) throws SQLException {
        int serviceId = getServiceId(service);
        
        if (number > 0) 
        {
             DatabaseService.executeUpdate("UPDATE Transactions SET finish=? WHERE bank_id=? AND service_id=? " +
                    "AND finish IS NULL AND process IS NOT NULL",
                    new Timestamp(System.currentTimeMillis()), id, serviceId);
        }
        else
        {
            DatabaseService.executeUpdate("UPDATE Transactions SET process=? WHERE id IN " +
                    "(SELECT id FROM Transactions WHERE bank_id=? AND service_id=? " +
                    "AND finish IS NULL AND process IS NULL ORDER BY entry ASC LIMIT 1)",
                    new Timestamp(System.currentTimeMillis()), id, serviceId);
        }      
    }

    public static int entry(int id, String service) throws SQLException {
        int serviceId = getServiceId(service);
        
        int ordinal = lastOrdinal(id, service);

        ordinal += 1;
        DatabaseService.executeUpdate("INSERT INTO Transactions (bank_id,service_id,ordinal,entry,process,finish)" +
        " VALUES (?, ?, ?, ?, NULL, NULL)", id, serviceId, ordinal, new Timestamp(System.currentTimeMillis()));
        return ordinal;
    }

    public static void getBankQueues(int id, BankData bank) throws SQLException {

        List<String> services = BankData.getServices();
        for(String service : services) {
            List<Integer> newList = new ArrayList<Integer>(2);          

            int ordinal = inProcess(id, service);
            newList.add(ordinal);

            ResultSet rs = DatabaseService.executeQuery("SELECT COUNT (*) AS total FROM Transactions T " +
                "LEFT JOIN Services s ON t.service_id = s.id WHERE T.bank_id=? AND S.name LIKE ? " +
                "AND T.finish IS NULL AND T.process IS NULL", id, service);
            if (rs != null && rs.next()) {
                newList.add(rs.getInt("total"));
            }
            else {
                newList.add(0);
            }
           bank.put(new Queue(service, newList));
        }
    }

}
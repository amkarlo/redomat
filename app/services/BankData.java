package services;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BankData {
    private int id;
    private String name;
    private String address;
    private List<Queue> info;

    private BankData(){ }

    public void addBankData(int id, String name, String address){
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = new ArrayList<Queue>();
    }

    public void put(Queue serviceQueue){
        this.info.add(serviceQueue);
    }

    public int getId(){
        return this.id;
    }   
    public String getName(){
        return this.name;
    }
    public String getAddress(){
        return this.address;
    }
    public List<Queue> getInfo(){
        return this.info;
    }

    public static List<String> getServices() throws SQLException {
        List<String> services = new ArrayList<String>();

        ResultSet rs = DatabaseService.executeQuery("SELECT name FROM Services");
        while (rs != null && rs.next()) {
            services.add(rs.getString("name"));
        }

        return services;
    }

    public static List<BankData> getAllBanksWithData() throws SQLException {
        List<BankData> banks = new ArrayList<BankData>();

        ResultSet rs = DatabaseService.executeQuery("SELECT id, name, address FROM Banks ORDER BY id");
        while (rs != null && rs.next()) {
            BankData bank = new BankData();
            bank.addBankData(rs.getInt("id"), rs.getString("name"), rs.getString("address"));
            Queue.getBankQueues(rs.getInt("id"), bank);
            banks.add(bank);
        }

        return banks;
    }

    public static BankData getBankWithData(int id) throws SQLException {
        BankData bank = new BankData();
        ResultSet rs = DatabaseService.executeQuery("SELECT name, address FROM Banks WHERE id=?", id);
        if (rs != null && rs.next()) {
            bank.addBankData(id, rs.getString("name"), rs.getString("address"));
            Queue.getBankQueues(id, bank);
        }

        return bank;

    }

}
package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.ChildAvailDAO;
import com.rm.pir.model.Child;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ChildAvailDAOImpl extends DBManager implements ChildAvailDAO {

//    private Connection con;
//    
//    public void open() throws SQLException {
//        con = super.getConnection();
//    }
//    
//    public void close() throws SQLException {
//        con.close();
//    }
    
    @Override
    public int insert(Child child) throws SQLException {
        try (Connection con = super.getConnection(); 
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.availability_child "
                                + "(childid, day_avail, hrs_avail) "
                                + "VALUES (?,?,?)")) {
            insert.setLong(1, child.getChildID());
            Map<String, List<String>> map = child.getDaymap();
            //use an iterator for loop to iterate over map
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                //get the key and values and set them, looping on the list
                List<String> value = entry.getValue();
                String key = entry.getKey();
                insert.setString(2, key);
                for (int i=0; i<value.size(); i++) {
                    insert.setString(3, value.get(i));
                    int result = insert.executeUpdate();
                    if (result != 1) throw new SQLException("Error adding to availibility_child");
                }
            }
        }
        
        return 1;
    }

    @Override
    public int update(Child child) throws SQLException {
        //this will be too complicated to update on this table
        //call delete then call insert
        int result = delete(child);
        return insert(child);
    }

    @Override
    public int delete(Child child) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.availability_child "
                                + "WHERE childID = ?")) {
            delete.setLong(1, child.getChildID());
            result = delete.executeUpdate();
        }
        
        return result;
    }

    @Override
    public Map<String, List<String>> findByID(long childID) throws SQLException {
        Map<String, List<String>> map;
        try (Connection con = super.getConnection()) {
            map = new LinkedHashMap<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.availability_child a "
                         + "WHERE a.childid = ?")) {
                find.setLong(1, childID);
                try (ResultSet rs = find.executeQuery()) {
                    while(rs.next()) {
                        String day = rs.getString("day_avail");
                        String hour = rs.getString("hrs_avail");
                        if (map.containsKey(day))
                            map.get(day).add(hour);
                        else {
                            List<String> newList = new ArrayList<>();
                            newList.add(hour);
                            map.put(day, newList);
                        }
                    }
                }
            }
        }
        
        return map;
    }
}

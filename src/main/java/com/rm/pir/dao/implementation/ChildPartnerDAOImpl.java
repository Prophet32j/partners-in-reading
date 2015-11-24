package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.ChildPartnerDAO;
import com.rm.pir.model.Partner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ChildPartnerDAOImpl extends DBManager implements ChildPartnerDAO {

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
    public Partner findByID(long childid) throws SQLException {
        Partner partner;
        try (Connection con = super.getConnection()) {
            partner = null;
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.child_requested_partner "
                         + "WHERE childid = ?")) {
                find.setLong(1, childid);
                try (ResultSet rs = find.executeQuery()) {
                    if (rs.next()) {
                        partner = new Partner();
                        partner.setID(childid);
                        partner.setFirstName(rs.getString("firstname"));
                        partner.setLastName(rs.getString("lastname"));
                        partner.setPhone(rs.getString("phone"));
                    }
                }
            }
        }
        
        return partner;
    }

    @Override
    public int delete(long childid) throws SQLException {
        int ret;
        try (Connection con = super.getConnection(); 
                PreparedStatement del = con.prepareStatement(
                                "DELETE FROM normal_partner.child_requested_partner "
                                + "WHERE childid = ?")) {
            del.setLong(1, childid);
            ret = del.executeUpdate();
        }
        
        return ret;
    }

    @Override
    public int update(Partner partner) throws SQLException {
        delete(partner.getID());
        return insert(partner);
    }

    @Override
    public int insert(Partner partner) throws SQLException {
        int ret;
        try (Connection con = super.getConnection(); 
                PreparedStatement ins = con.prepareStatement(
                                "INSERT INTO normal_partner.child_requested_partner "
                                + "VALUES(?,?,?,?)")) {
            ins.setLong(1, partner.getID());
            ins.setString(2, partner.getFirstName());
            ins.setString(3, partner.getLastName());
            ins.setString(4, partner.getPhone());
            ret = ins.executeUpdate();
        }
        
        return ret;
    }
}

package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.dao.interfaces.StudentPartnerDAO;
import com.rm.pir.model.Partner;
import com.rm.pir.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class StudentPartnerDAOImpl extends DBManager implements StudentPartnerDAO {

//    private Connection con;
    @Inject
    private StudentDAO dao;
    
//    public void open() throws SQLException {
//        con = super.getConnection();
//    }
//
//    public void close() throws SQLException {
//        con.close();
//    }
    
    @Override
    public List<Partner> findByID(long studentid) throws SQLException {
        List<Partner> partners;
        try (Connection con = super.getConnection()) {
            partners = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.student_requested_partner "
                         + "WHERE studentid = ?")) {
                find.setLong(1, studentid);
                try (ResultSet rs = find.executeQuery()) {
                    while (rs.next()) {
                        Partner partner = new Partner();
                        partner.setID(studentid);
                        partner.setFirstName(rs.getString("firstname"));
                        partner.setLastName(rs.getString("lastname"));
                        partner.setPhone(rs.getString("phone"));
                        
                        partners.add(partner);
                    }
                }
            }
        }
        
        return partners;
    }

    @Override
    public int delete(Partner partner) throws SQLException {
        int ret;
        try (Connection con = super.getConnection(); 
                PreparedStatement del = con.prepareStatement(
                                "DELETE FROM normal_partner.student_requested_partner "
                                + "WHERE studentid = ? "
                                + "AND lastname = ? "
                                + "AND firstname = ? "
                                + "AND phone = ?")) {
            del.setLong(1, partner.getID());
            del.setString(2, partner.getLastName());
            del.setString(3, partner.getFirstName());
            del.setString(4, partner.getPhone());
            ret = del.executeUpdate();
        }
        
        return ret;
    }

    @Override
    public int insert(Partner partner) throws SQLException {
        int ret;
        try (Connection con = super.getConnection(); 
                PreparedStatement ins = con.prepareStatement(
                                "INSERT INTO normal_partner.student_requested_partner "
                                + "VALUES(?,?,?,?)")) {
            ins.setLong(1, partner.getID());
            ins.setString(2, partner.getFirstName());
            ins.setString(3, partner.getLastName());
            ins.setString(4, partner.getPhone());
            ret = ins.executeUpdate();
        }
        
        return ret;
    }

    @Override
    public List<Partner> findAll() throws SQLException {
        List<Partner> all;
        try (Connection con = super.getConnection()) {
            all = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.student_requested_partner"); 
                    ResultSet rs = find.executeQuery()) {
                while (rs.next()) {
                    Student student = dao.findByID(rs.getLong("studentid"));
                    all.add(new Partner(student,
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("phone")));
                }
            }
        }
        
        return all;
    }
}

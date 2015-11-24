package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.AdminDAO;
import com.rm.pir.model.Admin;
import com.rm.pir.utilities.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AdminDAOImpl extends DBManager implements AdminDAO {

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
    public int insert(Admin admin) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.admin "
                                + "(email, lastname, firstname) "
                                + "VALUES (?,?,?)")) {
            insert.setString(1, admin.getEmail().toLowerCase());
            insert.setString(2, Util.sanitize(admin.getLastname().toLowerCase()));
            insert.setString(3, Util.sanitize(admin.getFirstname().toLowerCase()));
            result = insert.executeUpdate();
        }
        
        return result;
    }

    @Override
    public int update(Admin admin) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.admin a "
                                + "SET a.email = ? "
                                + "a.lastname = ? "
                                + "a.firstname = ? "
                                + "WHERE a.adminid = ?")) {
            update.setString(1, admin.getEmail());
            update.setString(2, Util.sanitize(admin.getLastname()));
            update.setString(3, Util.sanitize(admin.getFirstname()));
            update.setLong(4, admin.getAdminID());
            result = update.executeUpdate();
        }
        
        return result;
    }

    @Override
    public int delete(Admin admin) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.admin "
                                + "WHERE adminid = ?")) {
            delete.setLong(1, admin.getAdminID());
            result = delete.executeUpdate();
        }
        
        return result;
    }

    @Override
    public Admin findByID(long adminID) throws SQLException {
        Admin bean;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * "
                                + "FROM normal_partner.admin a "
                                + "WHERE a.adminid = ?")) {
            find.setLong(1, adminID);
            try (ResultSet rs = find.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                bean = new Admin();
                bean.setAdminID(rs.getLong("adminid"));
                bean.setEmail(rs.getString("email"));
                bean.setLastname(rs.getString("lastname"));
                bean.setFirstname(rs.getString("firstname"));
            }
        }
        
        return bean;
    }

    @Override
    public Admin findByEmail(String email) throws SQLException {
        Admin bean;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * "
                                + "FROM normal_partner.admin a "
                                + "WHERE a.email = ?")) {
            find.setString(1, email);
            try (ResultSet rs = find.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                bean = new Admin();
                bean.setAdminID(rs.getLong("adminid"));
                bean.setEmail(rs.getString("email"));
                bean.setLastname(rs.getString("lastname"));
                bean.setFirstname(rs.getString("firstname"));
            }
        }
        
        return bean;
    }

    @Override
    public Admin[] findAll() throws SQLException {
        Admin[] arr;
        try (Connection con = super.getConnection(); 
                Statement findAll = con.createStatement()) {
            String sql = "SELECT * FROM normal_partner.admin";
            try (ResultSet rs = findAll.executeQuery(sql)) {
                List<Admin> list = new ArrayList<>();
//                arr = null;
                while (rs.next()) {
                    Admin bean = new Admin();
                    bean.setAdminID(rs.getLong("adminid"));
                    bean.setEmail(rs.getString("email"));
                    bean.setLastname(rs.getString("lastname"));
                    bean.setFirstname(rs.getString("firstname"));
                    list.add(bean);
                }
                arr = (Admin[]) list.toArray();
            }
        }
        
        return arr;
    }
}

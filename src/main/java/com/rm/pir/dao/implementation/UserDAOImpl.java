package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.User;
import com.rm.pir.utilities.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.joda.time.DateTime;

@Named
@RequestScoped
public class UserDAOImpl extends DBManager implements UserDAO {

//    private Connection con;

//    public void open() throws SQLException {
//        con = super.getConnection();
//    }
//
//    public void close() throws SQLException {
//        con.close();
//    }

    @Override
    public int insert(User user) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement ins = con.prepareStatement(
                                "INSERT INTO normal_partner.users "
                                + "(email, password, created, acct_type, "
                                + "last_login, activated, activation_key) "
                                + "VALUES (?,?,?,?,?,?,?)")) {
            ins.setString(1, Util.sanitize(user.getEmail().toLowerCase()));
            ins.setString(2, Util.sanitize(user.getPassword()));
            ins.setDate(3, user.getCreated());
            ins.setString(4, user.getAcct_type());
            DateTime now = new DateTime();
            ins.setTimestamp(5, new Timestamp(now.getMillis()));
            ins.setBoolean(6, user.isActivated());
            ins.setString(7, user.getActivationKey());
            result = ins.executeUpdate();
        }
        
        return result;
    }

    @Override
    public int update(User user) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.users u "
                                + "SET u.password = ?, "
                                + "u.acct_type = ? "
                                + "WHERE u.email = ?")) {
            update.setString(1, user.getPassword());
            update.setString(2, user.getAcct_type());
            update.setString(3, Util.sanitize(user.getEmail()));
            result = update.executeUpdate();
        }
        
        return result;
    }
    
    @Override
    public int updateLoginTimestamp(User user) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.users u "
                                + "SET u.last_login = ? "
                                + "WHERE u.email = ?")) {
            DateTime now = new DateTime();
            update.setTimestamp(1, new Timestamp(now.getMillis()));
            update.setString(2, Util.sanitize(user.getEmail()));
            result = update.executeUpdate();
        }
        
        return result;
    }

    @Override
    public int changePassword(User user) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(     
                                "UPDATE normal_partner.users u "
                                + "SET u.password = ? "
                                + "WHERE u.email = ?")) {
            update.setString(1, user.getPassword());
            update.setString(2, Util.sanitize(user.getEmail()));
            result = update.executeUpdate();
        }
        
        return result;
    }
    
    @Override
    public int delete(User user) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.users "
                                + "WHERE email = ?")) {
            delete.setString(1, Util.sanitize(user.getEmail()));
            result = delete.executeUpdate();
        }
        
        return result;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users;
        try (Connection con = super.getConnection()) {
            users = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.users "
                         + "ORDER BY email"); 
                    ResultSet rs = find.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setAcct_type(rs.getString("acct_type"));
                    user.setCreated(rs.getDate("created"));
                    user.setLast_login(rs.getTimestamp("last_login"));
                    user.setActivated(rs.getBoolean("activated"));
                    users.add(user);
                }
            }
        }
        
        return users;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        User user;
        try (Connection con = super.getConnection()) {
            user = null;
            try (PreparedStatement find = con.prepareStatement(
                                "SELECT * "
                                + "FROM normal_partner.users u "
                                + "WHERE u.email = ?")) {
                find.setString(1, Util.sanitize(email));
                try (ResultSet rs = find.executeQuery()) {
                    if (rs.next()) {
                        user = new User();
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setAcct_type(rs.getString("acct_type"));
                        user.setCreated(rs.getDate("created"));
                        user.setLast_login(rs.getTimestamp("last_login"));
                        user.setActivationKey(rs.getString("activation_key"));
                        user.setActivated(rs.getBoolean("activated"));
                    }
                }
            }
        }
        
        return user;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        boolean exists;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT email "
                                + "FROM normal_partner.users "
                                + "WHERE email = ?")) {
            find.setString(1, Util.sanitize(email));
            try (ResultSet rs = find.executeQuery()) {
                exists = false;
                if (rs.next())
                    exists = true;
            }
        }
        
        return exists;
    }
    
    @Override
    public int updateEmail(String newEmail, User user) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.users "
                                + "SET email = ? "
                                + "WHERE email = ?")) {
            update.setString(1, Util.sanitize(newEmail));
            update.setString(2, Util.sanitize(user.getEmail()));
            result = update.executeUpdate();
        }
        
        return result;
    }

    @Override
    public User findByActivationKey(String key) throws SQLException {
        User user = null;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT email "
                                + "FROM normal_partner.users "
                                + "WHERE activation_key = ?")) {
            find.setString(1, Util.sanitize(key));
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) {
                    user = this.findByEmail(rs.getString("email"));
                }
            }
        }
        
        return user;
    }
    
    @Override
    public void activate(User user) throws SQLException {
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareCall(
                                "UPDATE normal_partner.users "
                                + "SET activated = ?, "
                                + "activation_key = ? "
                                + "WHERE email = ?")) {
            update.setBoolean(1, user.isActivated());
            update.setString(2, null);
            update.setString(3, Util.sanitize(user.getEmail()));
            
            update.executeUpdate();
        }
    }

    @Override
    public void deactivate(User user) throws SQLException {
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareCall(
                                "UPDATE normal_partner.users "
                                + "SET activated = ?, "
                                + "activation_key = ? "
                                + "WHERE email = ?")) {
            update.setBoolean(1, user.isActivated());
            update.setString(2, user.getActivationKey());
            update.setString(3, Util.sanitize(user.getEmail()));
            
            update.executeUpdate();
        }
    }
}
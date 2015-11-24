package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.model.Child;
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
public class ChildDAOImpl extends DBManager implements ChildDAO {

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
    public long insert(Child child) throws SQLException {
        long key;
        try (Connection con = super.getConnection(); 
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.child "
                                + "(email, lastname, firstname, gender, age, "
                                + "grade, homephone, cellphone, special_needs, "
                                + "pnt_gdn_one, pnt_gdn_two, notes, language_needs) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, child.getEmail());
            insert.setString(2, Util.sanitize(child.getLastname()));
            insert.setString(3, Util.sanitize(child.getFirstname()));
            insert.setString(4, child.getGender().toLowerCase());
            insert.setInt(5, child.getAge());
            insert.setString(6, child.getGrade());
            insert.setString(7, Util.sanitize(child.getHomephone()));
            insert.setString(8, Util.sanitize(child.getCellphone()));
            insert.setBoolean(9, child.isSpecial_needs());
            insert.setString(10, Util.sanitize(child.getParent_one()));
            insert.setString(11, Util.sanitize(child.getParent_two()));
            insert.setString(12, Util.sanitize(child.getNotes()));
            insert.setBoolean(13, child.isLanguage_needs());
            insert.executeUpdate();
            key = 0;
            try (ResultSet rs = insert.getGeneratedKeys()) {
                if (rs.next())
                    key = rs.getLong(1);
            }
        }
        
        return key;
    }

    @Override
    public int update(Child child) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.child c "
                                + "SET c.email = ?, "
                                + "c.lastname = ?,"
                                + "c.firstname = ?,"
                                + "c.gender = ?,"
                                + "c.age = ?,"
                                + "c.homephone = ?,"
                                + "c.cellphone = ?,"
                                + "c.special_needs = ?,"
                                + "c.language_needs = ?,"
                                + "c.pnt_gdn_one = ?,"
                                + "c.pnt_gdn_two = ?,"
                                + "c.notes = ?,"
                                + "c.grade = ? "
                                + "WHERE c.childid = ?")) {
            update.setString(1, child.getEmail());
            update.setString(2, child.getLastname());
            update.setString(3, child.getFirstname());
            update.setString(4, child.getGender());
            update.setInt(5, child.getAge());
            update.setString(6, child.getHomephone());
            update.setString(7, child.getCellphone());
            update.setBoolean(8, child.isSpecial_needs());
            update.setBoolean(9, child.isLanguage_needs());
            update.setString(10, child.getParent_one());
            update.setString(11, child.getParent_two());
            update.setString(12, child.getNotes());
            update.setString(13, child.getGrade());
            update.setLong(14, child.getChildID());
            result = update.executeUpdate();
        }
        
        return result;  
    }

    @Override
    public int delete(Child child) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.child "
                                + "WHERE childid = ?")) {
            delete.setLong(1, child.getChildID());
            result = delete.executeUpdate();
        }

        return result;
    }

    @Override
    public Child findByID(long childID) throws SQLException {
        Child bean;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * FROM normal_partner.child c "
                                + "WHERE c.childid = ?")) {
            find.setLong(1, childID);
            try (ResultSet rs = find.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                bean = new Child();
                bean.setChildID(rs.getLong("childid"));
                bean.setEmail(rs.getString("email"));
                bean.setLastname(rs.getString("lastname"));
                bean.setFirstname(rs.getString("firstname"));
                bean.setGender(rs.getString("gender"));
                bean.setHomephone(rs.getString("homephone"));
                bean.setCellphone(rs.getString("cellphone"));
                bean.setNotes(rs.getString("notes"));
                bean.setAge(rs.getInt("age"));
                bean.setGrade(rs.getString("grade"));
                bean.setSpecial_needs(rs.getBoolean("special_needs"));
                bean.setParent_one(rs.getString("pnt_gdn_one"));
                bean.setParent_two(rs.getString("pnt_gdn_two"));
                bean.setLanguage_needs(rs.getBoolean("language_needs"));
            }
        }
        
        return bean;
    }

    @Override
    public List<Child> findByEmail(String email) throws SQLException {
        List<Child> list;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * FROM normal_partner.child c "
                                + "WHERE c.email = ?")) {
            find.setString(1, Util.sanitize(email));
            try (ResultSet rs = find.executeQuery()) {
                list = new ArrayList<>();
                while (rs.next()) {
                    Child bean = new Child();
                    bean.setChildID(rs.getLong("childid"));
                    bean.setEmail(rs.getString("email"));
                    bean.setLastname(rs.getString("lastname"));
                    bean.setFirstname(rs.getString("firstname"));
                    bean.setGender(rs.getString("gender"));
                    bean.setHomephone(rs.getString("homephone"));
                    bean.setCellphone(rs.getString("cellphone"));
                    bean.setNotes(rs.getString("notes"));
                    bean.setAge(rs.getInt("age"));
                    bean.setGrade(rs.getString("grade"));
                    bean.setSpecial_needs(rs.getBoolean("special_needs"));
                    bean.setParent_one(rs.getString("pnt_gdn_one"));
                    bean.setParent_two(rs.getString("pnt_gdn_two"));
                    bean.setLanguage_needs(rs.getBoolean("language_needs"));
                    list.add(bean);
                }
            }
        }
        
        return list;
    }

    @Override
    public Child[] findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public int changeCell(Child child) throws SQLException {
//        open();
//        PreparedStatement update = con.prepareStatement(
//                "UPDATE normal_partner.child u "
//                + "SET u.cellphone = ? "
//                + "WHERE u.childid = ?");
//        update.setString(1, Util.sanitize(child.getCellphone()));
//        update.setLong(2, child.getChildID());
//
//        int result = update.executeUpdate();
//        update.close();
//        con.close();
//        
//        return result;
//    }
//
//    @Override
//    public int changeHome(Child child) throws SQLException {
//        open();
//        PreparedStatement update = con.prepareStatement(
//                "UPDATE normal_partner.child u "
//                + "SET u.homephone = ? "
//                + "WHERE u.childid = ?");
//        update.setString(1, Util.sanitize(child.getHomephone()));
//        update.setLong(2, child.getChildID());
//
//        int result = update.executeUpdate();
//        update.close();
//        con.close();
//        
//        return result;
//    }
//
//    @Override
//    public int changeNotes(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.notes = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, Util.sanitize(child.getNotes()));
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changeFName(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.firstname = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, Util.sanitize(child.getFirstname()));
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changeLName(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.lastname = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, Util.sanitize(child.getLastname()));
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changeAge(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.age = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setInt(1, child.getAge());
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changeGrade(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.grade = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, child.getGrade());
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changePar1(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.pnt_gdn_one = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, Util.sanitize(child.getParent_one()));
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changePar2(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.pnt_gdn_two = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, Util.sanitize(child.getParent_two()));
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }
//
//    @Override
//    public int changeGender(Child child) throws SQLException {
//        int result;
//        try (Connection con = super.getConnection(); 
//                PreparedStatement update = con.prepareStatement(
//                                "UPDATE normal_partner.child u "
//                                + "SET u.gender = ? "
//                                + "WHERE u.childid = ?")) {
//            update.setString(1, Util.sanitize(child.getGender()));
//            update.setLong(2, child.getChildID());
//            result = update.executeUpdate();
//        }
//        
//        return result;
//    }

    @Override
    public List<String> findAllAvailableChildEmails() throws SQLException {
        List<String> emails;
        try (Connection con = super.getConnection(); 
                PreparedStatement query = con.prepareStatement(
                                "SELECT DISTINCT email "
                                + "FROM normal_partner.child "
                                + "WHERE childid IN "
                                + "(SELECT childid FROM normal_partner.availability_child GROUP BY childid)"); ResultSet rs = query.executeQuery()) {
            emails = new ArrayList<>();
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        }
        
        return emails;
    }
}

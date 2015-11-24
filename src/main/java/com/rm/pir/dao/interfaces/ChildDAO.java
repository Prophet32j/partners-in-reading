package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Child;
import java.sql.SQLException;
import java.util.List;

public interface ChildDAO {

    public long insert(Child child) throws SQLException;
    public int update(Child child) throws SQLException;
    public int delete(Child child) throws SQLException;
    public Child findByID(long childID) throws SQLException;
    public List<Child> findByEmail(String email) throws SQLException;
    public Child[] findAll() throws SQLException;
    public List<String> findAllAvailableChildEmails() throws SQLException;
    
//    public int changeCell(Child child) throws SQLException;
//    public int changeHome(Child child) throws SQLException;
//    public int changeNotes(Child child) throws SQLException;
//    public int changeFName(Child child) throws SQLException;
//    public int changeLName(Child child) throws SQLException;
//    public int changeAge(Child child) throws SQLException;
//    public int changeGrade(Child child) throws SQLException;
//    public int changePar1(Child child) throws SQLException;
//    public int changePar2(Child child) throws SQLException;
//    public int changeGender(Child child) throws SQLException;
}

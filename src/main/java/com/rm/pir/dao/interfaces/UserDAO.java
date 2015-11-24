package com.rm.pir.dao.interfaces;

import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    
    public int insert(User user) throws SQLException;
    public int update(User user) throws SQLException;
    public int updateLoginTimestamp(User user) throws SQLException;
    public int changePassword(User user) throws SQLException;
    public int delete(User user) throws SQLException;
    public List<User> findAll() throws SQLException;
    public User findByEmail(String email) throws SQLException;
    public boolean existsByEmail(String email) throws SQLException;
    public int updateEmail(String newEmail, User user) throws SQLException;
    public User findByActivationKey(String key) throws SQLException;
    public void activate(User user) throws SQLException;
    public void deactivate(User user) throws SQLException;
}

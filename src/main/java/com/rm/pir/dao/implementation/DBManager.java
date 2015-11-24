package com.rm.pir.dao.implementation;

import com.rm.pir.utilities.Constants;
import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.sql.DataSource;

public abstract class DBManager {
    
    @Resource(name=Constants.DB_JNDI_NAME)
    private DataSource ds;
    
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

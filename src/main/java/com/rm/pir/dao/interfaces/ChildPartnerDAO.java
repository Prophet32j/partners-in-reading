package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Partner;
import java.sql.SQLException;

public interface ChildPartnerDAO {
    
    public Partner findByID(long childid) throws SQLException;
    public int delete(long childid) throws SQLException;
    public int update(Partner partner) throws SQLException;
    public int insert(Partner partner) throws SQLException;
}

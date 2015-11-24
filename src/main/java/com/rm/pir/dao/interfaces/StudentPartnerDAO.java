package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Partner;
import java.sql.SQLException;
import java.util.List;

public interface StudentPartnerDAO {
    
    public List<Partner> findByID(long studentid) throws SQLException;
    public List<Partner> findAll() throws SQLException;
    public int delete(Partner partner) throws SQLException;
    public int insert(Partner partner) throws SQLException;
}

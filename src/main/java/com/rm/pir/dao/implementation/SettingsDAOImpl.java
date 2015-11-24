package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.SettingsDAO;
import com.rm.pir.model.Settings;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.joda.time.DateTime;

@Named
@RequestScoped
public class SettingsDAOImpl extends DBManager implements SettingsDAO {

    @Override
    public Settings getSettings() throws SQLException {
        Settings settings;
        try (Connection con = super.getConnection()) {
            settings = null;
            try (PreparedStatement ps = con.prepareStatement(
                         "SELECT * FROM normal_partner.settings"); 
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean regOpen = rs.getBoolean("registration_open");
                    
                    // convert sql date to joda date
                    Date temp = rs.getDate("suspense_date");
                    DateTime suspenseDate = new DateTime(temp.getTime());
                    
                    temp = rs.getDate("start_date");
                    DateTime startDate = new DateTime(temp.getTime());
                    
                    temp = rs.getDate("end_date");
                    DateTime endDate = new DateTime(temp.getTime());
                    
                    settings = new Settings(regOpen, suspenseDate, startDate, endDate);
                }
            }
        }
        
        return settings;
    }

    @Override
    public void saveSettings(Settings settings) throws SQLException {
        try (Connection con = super.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "TRUNCATE normal_partner.settings")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                         "INSERT INTO normal_partner.settings VALUES(?,?,?,?)")) {
                ps.setBoolean(1, settings.isRegistrationOpen());
                ps.setDate(2, new Date(settings.getSuspenseDate().getMillis()));
                ps.setDate(3, new Date(settings.getStartDate().getMillis()));
                ps.setDate(4, new Date(settings.getEndDate().getMillis()));
                
                ps.executeUpdate();
            }
        }
    }
}
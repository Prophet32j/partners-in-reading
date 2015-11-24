/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Settings;
import java.sql.SQLException;

public interface SettingsDAO {
    
    public Settings getSettings() throws SQLException;
    public void saveSettings(Settings settings) throws SQLException;
}

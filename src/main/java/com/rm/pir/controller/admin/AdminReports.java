package com.rm.pir.controller.admin;

import com.rm.pir.dao.interfaces.AttendanceDAO;
import com.rm.pir.model.AttendanceReport;
import com.rm.pir.model.Student;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class AdminReports implements Serializable {
    
    private int month;
    private List<AttendanceReport> reports;
        
    @Inject
    private AttendanceDAO adao;
    
    @PostConstruct
    public void init() {
        month = -1;
        reports = new ArrayList<>();
    }
    
    public void search() {
        try {
            reports = adao.findReportByMonth(month);
        } catch (SQLException ex) {
            Logger.getLogger(AdminReports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reset() {
        month = -1;
        reports.clear();
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the reports
     */
    public List<AttendanceReport> getReports() {
        return reports;
    }

    /**
     * @param reports the reports to set
     */
    public void setReports(List<AttendanceReport> reports) {
        this.reports = reports;
    }
}

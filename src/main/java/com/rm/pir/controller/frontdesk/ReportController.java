package com.rm.pir.controller.frontdesk;

import com.rm.pir.dao.interfaces.AttendanceDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Attendance;
import com.rm.pir.model.Student;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowScoped;

@Named
@WindowScoped
public class ReportController implements Serializable {
    
    @Inject
    private AttendanceDAO adao;
    @Inject
    private StudentDAO sdao;
    
    private long studentid;
    private Student student;
    private List<Student> students;
    private int month;
    private int count;
    
    @PostConstruct
    public void init() {
        try {
            students = sdao.findAll();
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void submit() {
        try {
            List<Attendance> list = adao.findByMonth(studentid, month);
            count = list.size();
            student = sdao.findByID(studentid);
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reset() {
        month = 0;
        count = -1;
        student = null;
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<Student> students) {
        this.students = students;
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
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the studentid
     */
    public long getStudentid() {
        return studentid;
    }

    /**
     * @param studentid the studentid to set
     */
    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }
}

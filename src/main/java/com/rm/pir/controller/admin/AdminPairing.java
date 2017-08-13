package com.rm.pir.controller.admin;

import com.rm.pir.utilities.Mailer;
import com.rm.pir.dao.interfaces.ChildAvailDAO;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.PairingDAO;
import com.rm.pir.dao.interfaces.PendingDAO;
import com.rm.pir.dao.interfaces.StudentAvailDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Session;
import com.rm.pir.model.Student;
import com.rm.pir.utilities.Constants;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class AdminPairing implements Serializable {
    
    @Resource(name=Constants.DB_JNDI_NAME)
    private DataSource ds;
    
    private List<Student> studentList;
    private List<Child> childList;
    private List<String> ckeys;
    private List<String> skeys;
    private List<String> filters;
    private Map<String, List<String>> cmap;
    private Map<String, List<String>> smap;
    private Map<String, List<String>> pmap;
    private List<Session> sessions;
//    private List<String> days;
//    private List<String> hours;
    private String day;
    private String hour;
    private int command;
    private long selectedChild;
    private long selectedStudent;
    private Student sdetail;
    private Child cdetail;
    @Inject 
    private ChildDAO childdao;
    @Inject 
    private StudentDAO studentdao;
    @Inject 
    private StudentAvailDAO savaildao;
    @Inject
    private ChildAvailDAO cavaildao;
    @Inject
    private PairingDAO padao;
    @Inject 
    private PendingDAO pedao;
    
    private boolean searched;
    private int pairsCount;

    @PostConstruct
    public void create() {
        searched = false;
        command = -1;
        selectedChild = -1;
        selectedStudent = -1;
        //instantiate all lists
        childList = new ArrayList<>();
        studentList = new ArrayList<>();
        ckeys = new ArrayList<>();
        skeys = new ArrayList<>();
        pmap = new HashMap<>();
//        days = new ArrayList<>();
//        hours = new ArrayList<>();
        sessions = new ArrayList<>();
    }
    
//    public void changeDays() {
//        hours = pmap.get(day);
//    }
    
    public void changeChild() {
        try {
            pmap.clear();
            day = null;
            hour = null;
            
            if (command == 1) {
                studentList.clear();
                selectedStudent = -1;
                sdetail = null;
                searched = false;
            }
            cdetail = childdao.findByID(selectedChild);
            cmap = cavaildao.findByID(selectedChild);
            ckeys.clear();
            for (Map.Entry<String, List<String>> entry : cmap.entrySet()) {
                ckeys.add(entry.getKey());
            }
            if (selectedStudent != -1)
                this.findDaysAndHours();
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void changeStudent() {
        try {
            pmap.clear();
            sessions.clear();
            day = null;
            hour = null;
            if (command == 0) {
                childList.clear();
                selectedChild = -1;
                cdetail = null;
                searched = false;
            }
            sdetail = studentdao.findByID(selectedStudent);
            smap = savaildao.findByID(selectedStudent);
            skeys.clear();
            for (Map.Entry<String, List<String>> entry : smap.entrySet()) {
                skeys.add(entry.getKey());
            }
            // get the number of pairs for this student
            pairsCount = getPairsCount(selectedStudent);
            if (pairsCount > 0) {
                Student student = new Student();
                student.setStudentID(selectedStudent);
                for (Session pairing : padao.findByStudent(student))                     
                    sessions.add(pairing);
                for (Session pending : pedao.findByStudent(student))
                    sessions.add(pending);
            }
            // now populate days and hours in common if child is also selected
            if (selectedChild != -1)
                this.findDaysAndHours();
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void populateChildren() {
        try (Connection con = ds.getConnection()) {
            reset();
            command = 1;
            try (Statement find = con.createStatement(); 
                    ResultSet rs = find.executeQuery(
                                        "SELECT childid "
                                        + "FROM normal_partner.child c "
                                        + "WHERE c.childid NOT IN "
                                        + "(SELECT childid "
                                        + "   FROM normal_partner.pairs) "
                                        + "AND c.childid NOT IN "
                                        + "(SELECT childid "
                                        + "   FROM normal_partner.pending) "
                                        + "AND c.childid IN "
                                        + "(SELECT childid "
                                        + "   FROM normal_partner.availability_child)")) {
                while (rs.next()) {
                    //populate the child object from the DAO
                    Child child = childdao.findByID(rs.getLong("childid"));
                    childList.add(child);   //add the child to the list
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void populateStudents() {
        try (Connection con = ds.getConnection()) {
            reset();
            command = 0;
            try (Statement find = con.createStatement(); 
                    ResultSet rs = find.executeQuery(
                                        "SELECT studentid "
                                        + "FROM normal_partner.student s "
                                        + "WHERE s.studentid NOT IN "
                                        + "(SELECT studentid FROM normal_partner.pairs "
                                        + "UNION "
                                        + "SELECT studentid from normal_partner.pending) "
                                        + "AND s.studentid IN "
                                        + "(SELECT studentid "
                                        + "FROM normal_partner.availability_student)"
                                        + "AND s.ortn_complete")) {
                while (rs.next()) {
                    //populate the child object from the DAO
                    Student student = studentdao.findByID(rs.getLong("studentid"));
                    studentList.add(student);   //add the child to the list
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void find() {
        if (command == 1) //means that we are trying to find compatible students
            findStudents();
        else if (command == 0)
            findChildren(); //trying to find compatible children
        searched = true;
    }
    
    private void findStudents() {
        studentList.clear();
        try (Connection con = ds.getConnection()) {
//            Child child = childdao.findByID(selectedChild);
            Map<String, List<String>> map = cavaildao.findByID(selectedChild);
            String sql;
            if (filters.contains("spec") && filters.contains("lang")) {
                sql = findSpecLangStudentsSQL();
            } else if (filters.contains("spec")) {
                sql = findSpecStudentsSQL();
            } else if (filters.contains("lang")) {
                sql = findLangStudentsSQL();
            } else {
                sql = findStudentsSQL();
            }
            try (PreparedStatement find = con.prepareStatement(sql)) {
//                Map<String, List<String>> map = child.getDaymap();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    find.setString(1, entry.getKey());
                    List<String> list = entry.getValue();
                    //loop through the list
                    for(int i=0; i<list.size(); i++) {
                        find.setString(2, list.get(i));
                        try (ResultSet rs = find.executeQuery()) {
                            while (rs.next()) {
                                Student student = studentdao.findByID(rs.getLong("studentid"));
                                //make sure it's not already a part of the list
                                if (studentList.isEmpty()) {
                                    studentList.add(student);
                                }
                                else
                                    for (int j=0; j<studentList.size(); j++) {
                                        if (studentList.get(j).getStudentID() == student.getStudentID())
                                            break;
                                        if (j == studentList.size()-1) {
                                            studentList.add(student);
                                            break;
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void findChildren() {
        childList.clear();
        try (Connection con = ds.getConnection()) {
//            Student student = studentdao.findByID(selectedStudent);
            Map<String, List<String>> map = savaildao.findByID(selectedStudent);
            String sql = findChildrenSQL();
            try (PreparedStatement find = con.prepareStatement(sql)) {
//                Map<String, List<String>> map = student.getDaymap();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    find.setString(1, entry.getKey());
                    List<String> list = entry.getValue();
                    //loop through the list
                    for(int i=0; i<list.size(); i++) {
                        find.setString(2, list.get(i));
                        try (ResultSet rs = find.executeQuery()) {
                            while (rs.next()) {
                                Child child = childdao.findByID(rs.getLong("childid"));
                                //make sure it's not already a part of the list
                                if (childList.isEmpty()) {
                                    childList.add(child);
                                }
                                else
                                    for (int j=0; j<childList.size(); j++) {
                                        if (childList.get(j).getChildID() == child.getChildID())
                                            break;
                                        if (j == childList.size()-1) {
                                            childList.add(child);
                                            break;
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
    }
    
    public void pair() {
        try (Connection con = ds.getConnection();
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.pairs "
                                + "(studentid, childid, session_day, session_hour) "
                                + "VALUES (?,?,?,?)")) {
            insert.setLong(1, selectedStudent);
            insert.setLong(2, selectedChild);
            insert.setString(3, day);
            insert.setString(4, hour);
            insert.executeUpdate();
            
            // send email confirmation
            sendConfirmation(studentdao.findByID(selectedStudent),
                    childdao.findByID(selectedChild));
            reset();
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void reset() {
        cdetail = null;
        sdetail = null;
        selectedChild = -1;
        selectedStudent = -1;
        studentList.clear();
        childList.clear();
        pmap.clear();
        day = null;
        hour = null;
        searched = false;
//        days.clear();
//        hours.clear();
        sessions.clear();
    }
    
    public void findDaysAndHours() {
        try (Connection con = ds.getConnection();
                PreparedStatement find = con.prepareStatement(
                                "SELECT s.day_avail, s.hrs_avail " +
                                "FROM normal_partner.availability_student s, " +
                                "normal_partner.availability_child c " +
                                "WHERE s.day_avail=c.day_avail " +
                                "AND s.hrs_avail=c.hrs_avail " +
                                "AND s.studentid = ? " +
                                "AND c.childid = ? " +
                                "AND s.hrs_avail not in " +
                                "(select session_hour " +
                                "from normal_partner.pairs p " +
                                "where p.studentid = ? " +
                                "and p.session_day = s.day_avail) "
                        + "AND s.hrs_avail not in "
                        + "(select session_hour "
                        + "from normal_partner.pending pen "
                        + "where pen.studentid=? "
                        + "and pen.session_day = s.day_avail)")) {
            find.setLong(1, selectedStudent);
            find.setLong(2, selectedChild);
            find.setLong(3, selectedStudent);
            find.setLong(4, selectedStudent);
            try (ResultSet rs = find.executeQuery()) {
                pmap.clear();
                while (rs.next()) {
                    String day = rs.getString("day_avail");
                    String hour = rs.getString("hrs_avail");
                    if (pmap.containsKey(day)) {
                        pmap.get(day).add(hour);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(hour);
                        pmap.put(day, list);
                    }
                }
//                days = new ArrayList<>(pmap.keySet());
//                if (!pmap.isEmpty())
//                    Logger.getLogger(AdminPairing.class.getName()).log(Level.INFO, "Pmap not empty!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPairing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int getPairsCount(long studentid) throws SQLException {
        int count = 0;
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(paircountSQL)) {
            // set the 3 parameters
            for (int i=1; i<4; i++)
                ps.setLong(i, studentid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("pairs");
                }
            }
        }
        return count;
    }
    
    public void sendConfirmation(Student pairedStudent, Child pairedChild) {
        String to = pairedStudent.getEmail() + "," + pairedChild.getEmail();
        String subject = "Partners In Reading - You have been paired!";
        String body =
                "<html>"
                + "<body><center>"
                + "<h3>Pairing Information</h3>"
                + "<p>Don't forget to contact each other for information</p>"
                + "<p>Student's Name: " + pairedStudent.getFirstname() + " " + pairedStudent.getLastname() + "<br/>"
                + "Student's email: " + pairedStudent.getEmail() + "<br/>"
                + "Student's cellphone: " + pairedStudent.getCellphone() + "<br/>"
                + "<br/>"
                + "Child's Name: " + pairedChild.getFirstname() + " " + pairedChild.getLastname() + "<br/>"
                + "Child's Parent's Name: " + pairedChild.getParent_one() + "<br/>"
                + "Child's email: " + pairedChild.getEmail() + "<br/>"
                + "Child's cellphone: " + pairedChild.getCellphone() + "<br/>"
                + "Child's homephone: " + pairedChild.getHomephone() + "</p>"
                + "Here is your session time:" + "<br/>"
                + "Day: " + day + "<br/>"
                + "Time: " + hour + "<br/>"
                + "Thanks for participating!<br/>"
                + "The Partners In Reading Team"
                + "</center></body>"
                + "</html>";
        Mailer email = new Mailer(to, subject, body);
        email.send();
    }
    
    // SQL adds aggregates counts of matched pairs for student from pairs and pending table
    private String paircountSQL = 
            "select (select count(*) from normal_partner.pairs where studentid=?) + " +
            "(select count(*) from normal_partner.pending where studentid=?) + "
            + "(select count(*) from normal_partner.student_requested_partner where studentid=?) as pairs";
    
    private String findStudentsSQL() {
        if (filters.contains("all"))
            return "SELECT DISTINCT s.studentid "
                    + "FROM normal_partner.availability_student s "
                    + "JOIN normal_partner.student a "
                    + "ON s.studentid=a.studentid "
                    + "WHERE s.day_avail = ? "
                    + "AND s.hrs_avail = ? "
                    + "AND a.ortn_complete = true "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
        
        return "SELECT DISTINCT s.studentid "
                + "FROM normal_partner.availability_student s "
                + "JOIN normal_partner.student a "
                + "ON s.studentid=a.studentid "
                + "WHERE s.day_avail = ? "
                + "AND s.hrs_avail = ? "
                + "AND a.ortn_complete = true "
                + "AND s.studentid NOT IN "
                + "(SELECT studentid "
                + "FROM normal_partner.pairs) "
                + "AND s.studentid NOT IN "
                + "(SELECT studentid FROM normal_partner.pending) "
                + "AND s.studentid NOT IN "
                + "(SELECT studentid FROM normal_partner.student_requested_partner) "
                + "AND s.studentid IN "
                + "(SELECT studentid FROM normal_partner.availability_student)";
    }
    
    private String findSpecStudentsSQL() {
        if (filters.contains("all"))
            return "SELECT DISTINCT a.studentid "
                    + "FROM normal_partner.availability_student a "
                    + "JOIN normal_partner.student s "
                    + "ON a.studentid=s.studentid "
                    + "WHERE s.is_spec_ed = true "
                    + "AND s.ortn_complete = true "
                    + "AND a.day_avail = ? "
                    + "AND a.hrs_avail = ? "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
        else
            return "SELECT DISTINCT a.studentid "
                    + "FROM normal_partner.availability_student a "
                    + "JOIN normal_partner.student s "
                    + "ON a.studentid=s.studentid "
                    + "WHERE s.is_spec_ed = true "
                    + "AND s.ortn_complete = true "
                    + "AND a.day_avail = ? "
                    + "AND a.hrs_avail = ? "
                    + "AND a.studentid NOT IN "
                    + "(SELECT studentid "
                    + "FROM normal_partner.pairs) "
                    + "AND s.studentid NOT IN "
                    + "(SELECT studentid FROM normal_partner.pending) "
                    + "AND s.studentid NOT IN "
                    + "(SELECT studentid FROM normal_partner.student_requested_partner) "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
    }
    
    private String findLangStudentsSQL() {
        if (filters.contains("all"))
            return "SELECT DISTINCT a.studentid "
                    + "FROM normal_partner.availability_student a "
                    + "JOIN normal_partner.student s "
                    + "ON a.studentid=s.studentid "
                    + "WHERE s.is_lang_ed = true "
                    + "AND s.ortn_complete = true "
                    + "AND a.day_avail = ? "
                    + "AND a.hrs_avail = ? "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
        else
            return "SELECT DISTINCT a.studentid "
                    + "FROM normal_partner.availability_student a "
                    + "JOIN normal_partner.student s "
                    + "ON a.studentid=s.studentid "
                    + "WHERE s.is_lang_ed = true "
                    + "AND s.ortn_complete = true "
                    + "AND a.day_avail = ? "
                    + "AND a.hrs_avail = ? "
                    + "AND a.studentid NOT IN "
                    + "(SELECT studentid "
                    + "FROM normal_partner.pairs) "
                    + "AND s.studentid NOT IN "
                    + "(SELECT studentid FROM normal_partner.pending) "
                    + "AND s.studentid NOT IN "
                    + "(SELECT studentid FROM normal_partner.student_requested_partner) "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
    }
    
    private String findSpecLangStudentsSQL() {
        if (filters.contains("all"))
            return "SELECT DISTINCT a.studentid "
                    + "FROM normal_partner.availability_student a "
                    + "JOIN normal_partner.student s "
                    + "ON a.studentid=s.studentid "
                    + "WHERE s.is_lang_ed = true "
                    + "AND s.is_spec_ed = true "
                    + "AND s.ortn_complete "
                    + "AND a.day_avail = ? "
                    + "AND a.hrs_avail = ? "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
        else
            return "SELECT DISTINCT a.studentid "
                    + "FROM normal_partner.availability_student a "
                    + "JOIN normal_partner.student s "
                    + "ON a.studentid=s.studentid "
                    + "WHERE s.is_lang_ed = true "
                    + "AND s.is_spec_ed = true "
                    + "AND s.ortn_complete "
                    + "AND a.day_avail = ? "
                    + "AND a.hrs_avail = ? "
                    + "AND a.studentid NOT IN "
                    + "(SELECT studentid "
                    + "FROM normal_partner.pairs) "
                    + "AND s.studentid NOT IN "
                    + "(SELECT studentid FROM normal_partner.pending) "
                    + "AND s.studentid NOT IN "
                    + "(SELECT studentid FROM normal_partner.student_requested_partner) "
                    + "AND s.studentid IN "
                    + "(SELECT studentid FROM normal_partner.availability_student)";
    }
    
    private String findChildrenSQL() {
        if (filters.contains("all"))
            return "SELECT DISTINCT childid "
                    + "FROM normal_partner.availability_child c "
                    + "WHERE c.day_avail = ? "
                    + "AND c.hrs_avail = ? "
                    + "AND c.childid IN "
                    + "(SELECT childid FROM normal_partner.availability_child)";
        else
            return "SELECT DISTINCT childid "
                    + "FROM normal_partner.availability_child c "
                    + "WHERE c.day_avail = ? "
                    + "AND c.hrs_avail = ? "
                    + "AND c.childid IN "
                    + "(SELECT childid "
                    + "FROM normal_partner.availability_child) "
                    + "AND c.childid NOT IN "
                    + "(SELECT childid "
                    + " FROM normal_partner.pairs) "
                    + "AND c.childid NOT IN "
                    + "(SELECT childid FROM normal_partner.pending) "
                    + "AND c.childid NOT IN "
                    + "(SELECT childid FROM normal_partner.child_requested_partner)";
    }
    
    public long getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(long selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public long getSelectedChild() {
        return selectedChild;
    }

    public void setSelectedChild(long selectedChild) {
        this.selectedChild = selectedChild;
    }

    public Student getSdetail() {
        return sdetail;
    }

    public void setSdetail(Student sdetail) {
        this.sdetail = sdetail;
    }

    public Child getCdetail() {
        return cdetail;
    }

    public void setCdetail(Child cdetail) {
        this.cdetail = cdetail;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public List<String> getCkeys() {
        return ckeys;
    }

    public void setCkeys(List<String> ckeys) {
        this.ckeys = ckeys;
    }

    public List<String> getSkeys() {
        return skeys;
    }

    public void setSkeys(List<String> skeys) {
        this.skeys = skeys;
    }

    public Map<String, List<String>> getCmap() {
        return cmap;
    }

    public void setCmap(Map<String, List<String>> cmap) {
        this.cmap = cmap;
    }

    public Map<String, List<String>> getSmap() {
        return smap;
    }

    public void setSmap(Map<String, List<String>> smap) {
        this.smap = smap;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public Map<String, List<String>> getPmap() {
        return pmap;
    }

    public void setPmap(Map<String, List<String>> pmap) {
        this.pmap = pmap;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * @return the studentList
     */
    public List<Student> getStudentList() {
        return studentList;
    }

    /**
     * @param studentList the studentList to set
     */
    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    /**
     * @return the childList
     */
    public List<Child> getChildList() {
        return childList;
    }

    /**
     * @param childList the childList to set
     */
    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    /**
     * @return the searched
     */
    public boolean isSearched() {
        return searched;
    }

    /**
     * @param searched the searched to set
     */
    public void setSearched(boolean searched) {
        this.searched = searched;
    }

//    /**
//     * @return the days
//     */
//    public List<String> getDays() {
//        return days;
//    }
//
//    /**
//     * @param days the days to set
//     */
//    public void setDays(List<String> days) {
//        this.days = days;
//    }
//
//    /**
//     * @return the hours
//     */
//    public List<String> getHours() {
//        return hours;
//    }
//
//    /**
//     * @param hours the hours to set
//     */
//    public void setHours(List<String> hours) {
//        this.hours = hours;
//    }

    /**
     * @return the pairsCount
     */
    public int getPairsCount() {
        return pairsCount;
    }

    /**
     * @param pairsCount the pairsCount to set
     */
    public void setPairsCount(int pairsCount) {
        this.pairsCount = pairsCount;
    }

    /**
     * @return the sessions
     */
    public List<Session> getSessions() {
        return sessions;
    }

    /**
     * @param sessions the sessions to set
     */
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
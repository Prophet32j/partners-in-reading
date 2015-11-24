package com.rm.pir.automation;

import com.rm.pir.utilities.Constants;
import com.rm.pir.utilities.MailSender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AutoPair implements Runnable {

    // student and child pool for pairing
    private List<Student> studentPool;
    private Queue<Child> childPool;
    // day/hour pool of time slots with available students and children
    private Map<String, List<Hour>> dayPool;
    private int numberPaired;
    
    private DataSource ds;
    private Connection con;
    
    public AutoPair() {
        studentPool = new ArrayList<>();
        childPool = new PriorityQueue<>();
        dayPool = new HashMap<>();
        numberPaired = 0;
        try {
            ds = (DataSource)new InitialContext().lookup(Constants.DB_JNDI_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(AutoPair.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            con = ds.getConnection();
            fillStudents(); 
            fillChildren();
            if (studentPool.isEmpty() || childPool.isEmpty()) // no one to pair
                return;
            while (!dayPool.isEmpty() || !childPool.isEmpty()) {
                removeEmptyHours();
                removeEmptyDays();
                if (!childPool.isEmpty())    // only call if someone to pair
                    pairPartners();
            }
            // email the admin
            if (numberPaired > 0) {
                String to = Constants.ADMIN_NPL_EMAIL;
                String subject = "Partners In Reading - Pending Pairs Need Approval";
                String body = "You have Partner Pairs that need Approval";
                new MailSender(to, subject, body).send();
            }
        } catch(Exception ex) { 
            Logger.getLogger(AutoPair.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                con.close();
                Logger.getLogger(AutoPair.class.getName()).log(Level.INFO, "AutoPair closing con");
            } catch (SQLException ex) {
                Logger.getLogger(AutoPair.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class Student implements Comparable {
        long id;
        int pairs;
        int slots;
        boolean isSpec;
        boolean isLang;
        boolean twoChildren;
        Map<String, List<String>> map;
        public Student() {
            id=-1;pairs=0;slots=0;isSpec=false;twoChildren=false;
            map = null;
        }
        
        @Override
        public boolean equals(Object o) {
            Student s = (Student) o;
            return s.id == this.id;
        }

        @Override
        public int compareTo(Object o) {
            Student s = (Student) o;
            if (pairs != s.pairs)
                return pairs - s.pairs;
            return slots - s.slots;
        }
    }
    private class Child implements Comparable {
        long id;
        int slots;
        boolean isSpec;
        boolean isLang;
        Map<String, List<String>> map;
        public Child() {
            id=-1;slots=0;isSpec=false;isLang=false;
            map = null;
        }
        
        @Override
        public boolean equals(Object o) {
            Child c = (Child) o;
            return c.id == this.id;
        }

        @Override
        public int compareTo(Object o) {
            Child c = (Child) o;
            return slots - c.slots;
        }
    }
    private class Hour {
        String hour;
        Queue<Student> students;
        Queue<Child> children;
        
        public Hour() {
            this(null);
        }
        public Hour(String h) {
            hour = h;
            students = new PriorityQueue<>();
            children = new PriorityQueue<>();
        }
        
        @Override
        public boolean equals(Object o) {
            Hour h = (Hour) o;
            return h.hour.equals(hour);
        }
    }
    
    // SQL selects all student attributes where student has availability
    // and student has completed orientation
    private String studentSQL = 
            "select distinct s.studentid, a.is_lang_ed, a.is_spec_ed, a.two_chldn " +
            "from normal_partner.availability_student s join normal_partner.student a on s.studentid=a.studentid "
            + "where a.ortn_complete = true";
    
    // SQL adds aggregates counts of matched pairs for student from pairs and pending table
    private String paircountSQL = 
            "select (select count(*) from normal_partner.pairs where studentid=?) + " +
            "(select count(*) from normal_partner.pending where studentid=?) + "
            + "(select count(*) from normal_partner.student_requested_partner where studentid=?) as pairs";
    
    // SQL selects all children attributes where child isn't in pairs or pending table
    private String childrenSQL =
            "select distinct a.childid, c.special_needs, c.language_needs " +
            "from normal_partner.availability_child a join normal_partner.child c on a.childid=c.childid " +
            "where a.childid not in " +
            "(select childid from normal_partner.pairs) "
            + "and a.childid not in "
            + "(select childid from normal_partner.pending) "
            + "and a.childid not in "
            + "(select childid from normal_partner.child_requested_partner)";
    
    // SQL selects all days and hours of the child
    private String childavailSQL = 
            "select day_avail, hrs_avail " +
            "from normal_partner.availability_child a " +
            "where a.childid=?";
    
    // SQL selects all days and hours for student
    private String studentavailSQL = 
            "select a.day_avail, a.hrs_avail " +
            "from normal_partner.availability_student a " +
            "where a.studentid=?";
    
    // SQL select all pairs for student
    private String studentPairsSQL = 
            "select * from normal_partner.pairs "
            + "where studentid=?";
    
    // SQL select all pending pairs for student
    private String studentPendingSQL = 
            "select * from normal_partner.pending "
            + "where studentid=?";
    
    private void pairPartners() throws SQLException {
        // get child from the queue, don't remove yet
        Child child = childPool.peek();
        
        // find first available time for child
        Map<String, List<String>> cmap = child.map;
        Iterator<Map.Entry<String, List<String>>> mapEntryIter = cmap.entrySet().iterator();
        while (mapEntryIter.hasNext()) {
            Map.Entry<String, List<String>> mapEntry = mapEntryIter.next();
            
            String day = mapEntry.getKey();
            List<String> hoursOfDay = mapEntry.getValue();

            // find first intersection between hoursOfDay and hours lists
            // where students are present
            List<Hour> hours = dayPool.get(day);   // list of hours in the day of dayMap
            Hour checkHour = new Hour();
            Hour targetHour = null;
            
            // currently, intersection algorithm is O(n-squared), could be better?
            for (String hour : hoursOfDay) {
                checkHour.hour = hour;
                for (Hour objHour : hours) {
                    if (checkHour.equals(objHour) && !objHour.students.isEmpty()) {
                        targetHour = objHour;
                        break;
                    }
                }
                if (targetHour != null)
                    break;
            }
            
            // check to make sure we have found an intersection
            if (targetHour == null)
                continue;
//            String hour = mapEntry.getValue().get(0);
            
//            int index = hours.indexOf(new Hour(hour));
//            Hour targetHour = hours.get(index);
            // now we need to find the student with lowest pairs and slots
            Student student;
            Queue<Student> studentQueue = new PriorityQueue<>();
            if (child.isSpec) {
                // find studens who are spec ed only
                // only pair spec needs children with spec ed students
                for (Student targetStudent : targetHour.students) {
                    if(targetStudent.isSpec)
                        studentQueue.add(targetStudent);
                }
                if (studentQueue.isEmpty()) // no students, get another hour
                    continue;
                student = studentQueue.remove();
            } else if (child.isLang) {
                // find students who are spec ed or lang ed
                // only pair language needs children with speciaty students
                for (Student targetStudent : targetHour.students) {
                    if(targetStudent.isLang || targetStudent.isSpec)
                        studentQueue.add(targetStudent);
                }
                if (studentQueue.isEmpty()) // no students, get another child
                    continue;
                student = studentQueue.remove();
            } else {    // normal child, pair normally
                student = targetHour.students.remove();
            }
            int result;
            try (PreparedStatement stmt = con.prepareStatement(
                         "INSERT INTO normal_partner.pending "
                         + "(studentid, childid, session_day, session_hour) "
                         + "VALUES(?,?,?,?)")) {
                stmt.setLong(1, student.id);
                stmt.setLong(2, child.id);
                stmt.setString(3, day);
                stmt.setString(4, targetHour.hour);
                result = stmt.executeUpdate();
            }
            if (result != 1) 
                throw new SQLException("Something went wrong writing pair to pending");
            
            student.pairs++;
            // remove from pool if student only wants 1 pairing or student has 2 pairs
            if (!student.twoChildren || student.pairs == 2) {
                removeStudent(student);
            } else {
                targetHour.students.remove(student);
                // check to see if we need to remove student's next available time slot
                // reading sessions are 45 minutes long, time slots are 30 minutes apart
                checkAndRemovePoolSlots(student, day, targetHour.hour);
            }
            numberPaired++;  // increment number of pairs performed today
            break;
        }
        removeChild(child); // now remove the child from the pool
    }
    
    /**
     * checks to see if student belongs to any adjacent pool slots and removes
     * student from those slots
     * @param student
     * @param day
     * @param hour 
     */
    private void checkAndRemovePoolSlots(Student student, String day, String hour) {
        List<Hour> poolHours = dayPool.get(day);
        switch (hour) {
            case "3:00pm":
                removeStudent("3:30pm", student, poolHours);
                break;
            case "3:30pm":
                removeStudent("3:00pm", student, poolHours);
                removeStudent("4:00pm", student, poolHours);
                break;
            case "4:00pm":
                removeStudent("3:30pm", student, poolHours);
                removeStudent("4:30pm", student, poolHours);
                break;
            case "4:30pm":
                removeStudent("4:00pm", student, poolHours);
                removeStudent("5:00pm", student, poolHours);
                break;
            case "5:00pm":
                removeStudent("4:30pm", student, poolHours);
                removeStudent("5:30pm", student, poolHours);
                break;
            case "5:30pm":
                removeStudent("5:00pm", student, poolHours);
                removeStudent("6:00pm", student, poolHours);
                break;
            case "6:00pm":
                removeStudent("5:30pm", student, poolHours);
                removeStudent("6:30pm", student, poolHours);
                break;
            case "6:30pm":
                removeStudent("6:00pm", student, poolHours);
                removeStudent("7:00pm", student, poolHours);
                break;
            case "7:00pm":
                removeStudent("6:30pm", student, poolHours);
                removeStudent("7:30pm", student, poolHours);
                break;
            case "7:30pm":
                removeStudent("7:00pm", student, poolHours);
                removeStudent("8:00pm", student, poolHours);
                break;
        }
    }
    
    /**
     * removes this student from all pairing lists and queues so that student 
     * can't be paired again
     * @param student to be removed form pairing pool
     */
    private void removeStudent(Student student) {
        // remove student from all queues and lists
        // iterate over the student's map and remove student from dayPool
        for (Map.Entry<String, List<String>> entry : student.map.entrySet()) {
            String dayFromStudent = entry.getKey();
            // get the list of Hours from the dayPool for day from student
            List<Hour> hoursFromPool = dayPool.get(dayFromStudent);
            // check to make sure list isn't empty
            if (hoursFromPool != null) {
                // get the list of hours from student for given day
                List<String> hoursListFromStudent = entry.getValue();
                // iterate over hours and remove student from each given hour
                // from the dayPool
                for (String hourFromStudent : hoursListFromStudent) {
                    Hour hour = new Hour(hourFromStudent);
                    int index = hoursFromPool.indexOf(hour);
                    if (index > -1)
                        hoursFromPool.get(index).students.remove(student);
                }
            }
        }
    }
    
    private void removeStudent(String hour, Student student, List<Hour> pool) {
        Hour temp = new Hour(hour);
        int index = pool.indexOf(temp);
        if (index > -1)
            pool.get(index).students.remove(student);
    }
    
    /**
     * checks to see if student has an immediate adjacent time slot that
     * must be removed to not cause conflicts in consecutive pairing sessions
     * @param student
     * @param day
     * @param hour 
     */
    private void checkAndRemoveAvailabilitySlots(
            Map<String, List<String>> availability, 
            String day, 
            String hour) {
        
        List<String> hours = availability.get(day);
        // determine if the list has an adjacent time slot
        // if it does, remove it
        switch (hour) {
            case "3:00pm":
                hours.remove("3:30pm");
                break;
            case "3:30pm":
                hours.remove("3:00pm");
                hours.remove("4:00pm");
                break;
            case "4:00pm":
                hours.remove("3:30pm");
                hours.remove("4:30pm");
                break;
            case "4:30pm":
                hours.remove("4:00pm");
                hours.remove("5:00pm");
                break;
            case "5:00pm":
                hours.remove("4:30pm");
                hours.remove("5:30pm");
                break;
            case "5:30pm":
                hours.remove("5:00pm");
                hours.remove("6:00pm");
                break;
            case "6:00pm":
                hours.remove("5:30pm");
                hours.remove("6:30pm");
                break;
            case "6:30pm":
                hours.remove("6:00pm");
                hours.remove("7:00pm");
                break;
            case "7:00pm":
                hours.remove("6:30pm");
                hours.remove("7:30pm");
                break;
            case "7:30pm":
                hours.remove("7:00pm");
                hours.remove("8:00pm");
                break;
        }
    }
    
    private void fillStudents() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(studentSQL); 
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student s = new Student();
                s.id = rs.getLong("studentid");
                s.isSpec = rs.getBoolean("is_spec_ed");
                s.isLang = rs.getBoolean("is_lang_ed");
                s.twoChildren = rs.getBoolean("two_chldn");
                s.pairs = getStudentPairsCount(s.id);
                if (s.twoChildren && s.pairs < 2 || !s.twoChildren && s.pairs < 1) {
                    s.map = fillStudentMap(s);
                    // count hours
                    for (Map.Entry<String, List<String>> entry : s.map.entrySet()) {
                        int size = entry.getValue().size();
                        s.slots += size;
                    }
                    // add student to the pools
                    addStudentToDayPool(s);
                    studentPool.add(s);
                }
            }
        }
    }
    
    private void fillChildren() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(childrenSQL); 
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Child c = new Child();
                c.id = rs.getLong("childid");
                c.isSpec = rs.getBoolean("special_needs");
                c.isLang = rs.getBoolean("language_needs");
                c.map = fillChildMap(c);
                // now we need to count how many hours there are in each day
                for (Map.Entry<String, List<String>> entry : c.map.entrySet()) {
                    int size = entry.getValue().size();
                    c.slots += size;
                }
                childPool.add(c);
            }
        }
    }
    
    private Map<String, List<String>> fillStudentMap(Student student) throws SQLException {
        Map<String, List<String>> availability = new HashMap<>();
        try (PreparedStatement availabilityStmt = con.prepareStatement(studentavailSQL)) {
            availabilityStmt.setLong(1, student.id);
            try (ResultSet availabilityRs = availabilityStmt.executeQuery()) {
                while (availabilityRs.next()) {
                    String day = availabilityRs.getString("day_avail");
                    String hour = availabilityRs.getString("hrs_avail");
                    if (availability.containsKey(day))
                        availability.get(day).add(hour);
                    else {
                        List<String> list = new ArrayList<>();
                        list.add(hour); 
                        availability.put(day, list);
                    }
                }
            }
        }
        
        return removeConflictsFromAvailability(availability, student);
    }
    
    private Map<String, List<String>> fillChildMap(Child child) throws SQLException {
        Map<String, List<String>> availability = new HashMap<>();
        try (PreparedStatement stmt = con.prepareStatement(childavailSQL)) {
            stmt.setLong(1, child.id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString("day_avail");
                    String hour = rs.getString("hrs_avail");
                    if (availability.containsKey(day))
                        availability.get(day).add(hour);
                    else {
                        List<String> hours = new ArrayList<>();
                        hours.add(hour);
                        availability.put(day, hours);
                    }
                    // check to see if pool already contains this day
                    Hour newHour = new Hour(hour);
                    if (dayPool.containsKey(day)) {
                        List<Hour> hours = dayPool.get(day);    // get the hours list for the day
                        if (hours.contains(newHour)) { // add day with hour list to map
                            Hour h = hours.get(hours.indexOf(newHour));
                            h.children.add(child);
                        } else {
                            newHour.children.add(child);
                            hours.add(newHour);
                        }
                    } else {    // pool doesn't contain day, add it
                        List<Hour> hours = new ArrayList<>();
                        newHour.children.add(child);   // add child to hour
                        hours.add(newHour);    // add hour to list
                        dayPool.put(day, hours);    // add day with hour list to map
                    }
                }
            }
        }
        
        return availability;
    }
    
    private int getStudentPairsCount(long id) throws SQLException {
        int count = 0;
        try (PreparedStatement stmt = con.prepareStatement(paircountSQL)) {
            stmt.setLong(1, id);
            stmt.setLong(2, id);
            stmt.setLong(3, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    count = rs.getInt("pairs");
                }
            }
        }
        return count;
    }
    
    private void removeEmptyHours() {
        // see if there are empty lists within the hours of pool
        for (Map.Entry<String, List<Hour>> dayEntry : dayPool.entrySet()) {
            Iterator<Hour> hourIter = dayEntry.getValue().iterator();
            while (hourIter.hasNext()) {
                Hour h = hourIter.next();
                
                if (h.students.isEmpty()) {
                    removeHourFromChildren(dayEntry.getKey(), h.hour);
                    hourIter.remove();
                } else if (h.children.isEmpty()) {
                    removeHourFromStudents(dayEntry.getKey(), h.hour);
                    hourIter.remove();
                }
            }
        }
    }
    
    private void removeHourFromChildren(String day, String hour) {
        Iterator<Child> citer = childPool.iterator();
        while (citer.hasNext()) {
            Child c = citer.next();
            if (c.map.containsKey(day)) {
                List<String> list = c.map.get(day);
                Iterator<String> iter = list.iterator();
                while (iter.hasNext()) {    // iterate over map to remove hour
                    String h = iter.next();
                    if (h.equals(hour)) {
                        iter.remove();
                        c.slots--;
                        break;
                    }
                }
                if (list.isEmpty()) // determine if we just removed the last hour from the day
                    c.map.remove(day);
            }
            if (c.map.isEmpty())
                citer.remove();
        }
    }
    
    private void removeHourFromStudents(String day, String hour) {
        Iterator<Student> siter = studentPool.iterator();
        while (siter.hasNext()) {
           Student s = siter.next();
            if (s.map.containsKey(day)) {
                List<String> list = s.map.get(day);
                Iterator<String> iter = list.iterator();
                while (iter.hasNext()) {    // iterate over map to remove hour
                    String h = iter.next();
                    if (h.equals(hour)) {
                        iter.remove();
                        s.slots--;
                        break;
                    }
                }
                if (list.isEmpty()) // determine if we just removed the last hour from the day
                    s.map.remove(day);
            }
            if (s.map.isEmpty())
                siter.remove();
        }
    }
    
    private void removeEmptyDays() {
        // get keys and see if any days have empty lists
        Set<String> daySet = dayPool.keySet();
        Iterator<String> iter = daySet.iterator();
        while (iter.hasNext()) {
            String day = iter.next();
            if (dayPool.containsKey(day)) {
                if (dayPool.get(day).isEmpty()) {
                    iter.remove();
                    removeDayFromStudents(day);
                    removeDayFromChildren(day);
                }
            }
        }
    }
    
    private void removeDayFromStudents(String day) {
        for (int i=0; i<studentPool.size(); i++) {
            Student s = studentPool.get(i);
            if (s.map.containsKey(day)) {
                s.map.remove(day);
            }
        }
    }
    
    private void removeDayFromChildren(String day) {
        for (Child child : childPool) {
            child.map.remove(day);
        }
    }
    
    /**
     * removes this child from the child pool and the day/hour pool
     * @param child 
     */
    private void removeChild(Child child) {
        childPool.remove(child);
        // iterate over map and remove this child from all hours in pool
        for (Map.Entry<String, List<String>> entry : child.map.entrySet()) {
            List<String> list = entry.getValue();
            String day = entry.getKey();
            if (dayPool.containsKey(day)) {
                List<Hour> hours = dayPool.get(day);
                for (int i=0; i<list.size(); i++) {
                    String hour = list.get(i);
                    int index = hours.indexOf(new Hour(hour));
                    if (index > -1) {
                        hours.get(index).children.remove(child);
                    }
                }
            }
        }
    }
    
    /**
     * retrieves current pairs and pending pairs from database and removes
     * any adjacent time slots for each pair and pending pair
     * @param availability
     * @param student
     * @return 
     */
    private Map<String, List<String>> removeConflictsFromAvailability(
            Map<String, List<String>> availability, 
            Student student) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(studentPairsSQL)) {
            stmt.setLong(1, student.id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString("session_day");
                    String hour = rs.getString("session_hour");
                    // remove adjacent time slots for day/hour AND remove day/hour
                    if (availability.containsKey(day)) {
                        checkAndRemoveAvailabilitySlots(availability, day, hour);
                        availability.get(day).remove(hour);
                    }
                }
            }
        }
        try (PreparedStatement stmt = con.prepareStatement(studentPendingSQL)) {
            stmt.setLong(1, student.id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString("session_day");
                    String hour = rs.getString("session_hour");
                    // remove adjacent time slots for day/hour AND remove day/hour
                    if (availability.containsKey(day)) {
                        checkAndRemoveAvailabilitySlots(availability, day, hour);
                        availability.get(day).remove(hour);
                    }
                }
            }
        }
        
        return availability;
    }
    
    /**
     * for each day/hour in student's availability, adds this student
     * to the corresponding day/hour of this dayPool
     * @param student 
     */
    private void addStudentToDayPool(Student student) {
        Map<String, List<String>> availability = student.map;
        // iterate over the availablity map and add student to pool
        for (Entry<String, List<String>> entry: availability.entrySet()) {
            String availableDay = entry.getKey();
            List<String> availableHours = entry.getValue();
            
            if (dayPool.containsKey(availableDay)) {
                List<Hour> hoursPool = dayPool.get(availableDay);
                // for each hour, check pool for the hour and add
                for (String hour : availableHours) {
                    Hour availableHour = new Hour(hour);
                    if (hoursPool.contains(availableHour)) { // add student
                        hoursPool.get(hoursPool.indexOf(availableHour))
                                .students
                                .add(student);
                    } else { // does not exist, add new Hour
                        availableHour.students.add(student);
                        hoursPool.add(availableHour);
                    }
                }
            } else {
                List<Hour> newHoursPool = new ArrayList<>();
                for (String hour : availableHours) {
                    Hour newHour = new Hour(hour);
                    newHour.students.add(student);
                    newHoursPool.add(newHour);
                }
                dayPool.put(availableDay, newHoursPool);
            }
        }
    }
}

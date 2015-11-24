package com.rm.pir.automation;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BackgroundJobManager implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new EmailReminder(),
                                    getMillisUntilDesiredHour(5),
//                                    5000,
                                    86400000, 
                                    TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new AutoPair(),
                                    getMillisUntilDesiredHour(0), 
//                                    5000,
                                    86400000, 
                                    TimeUnit.MILLISECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdownNow();
    } 
    
    private long getMillisUntilDesiredHour(int desiredHour) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, desiredHour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTimeInMillis() - now.getTime();
    }
}

package com.example.schoolmanagement.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {


    @Scheduled(cron = "0 0 1 * * ?")
    public void runDailyReportTask() {
        System.out.println("Running daily report task at 1 AM");
    }


    @Scheduled(fixedRate = 300000)
    public void runPeriodicCleanupTask() {
        System.out.println("Running periodic cleanup task every 5 min");

    }
}

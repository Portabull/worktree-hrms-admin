package com.worktree.hrms;

import com.worktree.hrms.exceptions.WorktreeException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WorktreeHrmsAdminApplication {


    static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(WorktreeHrmsAdminApplication.class, args);
    }

    public static void restart() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                context.close();
                context = SpringApplication.run(WorktreeHrmsAdminApplication.class);
            } catch (Exception e) {
                throw new WorktreeException("Restart Failed", e);
            }
        });
        thread.setDaemon(false);
        thread.start();
    }

}

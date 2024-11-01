package com.herokudevrel.agentforce.collageagent.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
public class DirectoryCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryCleanupTask.class);

    private static final String DIRECTORY_PATH = "./downloads";
    private static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(24); // 24 hours

    @Scheduled(fixedRate = 3600000) // Runs every hour (3600000 ms)
    public void cleanUpOldFiles() {
        File directory = new File(DIRECTORY_PATH);
        if (directory.isDirectory()) {
            long currentTime = System.currentTimeMillis();
            for (File file : directory.listFiles()) {
                if (file.isFile() && (currentTime - file.lastModified() > EXPIRATION_TIME)) {
                    if (file.delete()) {
                        logger.info("Deleted old file: {}", file.getName());
                    }
                }
            }
        }
    }
}

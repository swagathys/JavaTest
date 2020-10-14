package com.credit.suisse.javaTest;

import com.credit.suisse.javaTest.service.EventParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class JavaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaTestApplication.class, args);
    }

    @Component
    class StartupApplicationListener {

        @Autowired
        EventParser eventParser;

        @EventListener
        public void onApplicationEvent(ContextRefreshedEvent event) throws IOException {
            File file = new File(JavaTestApplication.class.getClassLoader().getResource("logfile.txt").getFile());
            eventParser.parse(file);
        }
    }
}

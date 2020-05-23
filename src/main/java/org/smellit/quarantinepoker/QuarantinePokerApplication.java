package org.smellit.quarantinepoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class QuarantinePokerApplication implements CommandLineRunner {

    private TestService testService;

    @Autowired
    public QuarantinePokerApplication(TestService testService) {
        this.testService = testService;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuarantinePokerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        testService.add();
        testService.print();

    }
}

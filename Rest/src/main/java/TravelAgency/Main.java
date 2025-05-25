package TravelAgency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@ComponentScan({"travel", "TravelAgency"})
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean(name="properties")
    @Primary
    public Properties getBdProperties(){
        Properties props = new Properties();
        try {
            System.out.println("Searching bd.config in directory "+((new File(".")).getAbsolutePath()));
            File configFile = new File("bd.config");

            if (configFile.exists()) {
                props.load(new FileReader(configFile));
                System.out.println("Successfully loaded database properties:");
                System.out.println("JDBC URL: " + props.getProperty("jdbc.url"));
                System.out.println("JDBC Driver: " + props.getProperty("jdbc.driver"));
            } else {
                System.err.println("Configuration file bd.config not found at " + configFile.getAbsolutePath());
                // Set default properties
                props.setProperty("jdbc.driver", "org.sqlite.JDBC");
                props.setProperty("jdbc.url", "jdbc:sqlite:D:/UBB/semestrul 4/MPP/UML P1/turism.db");
                props.setProperty("jdbc.username", "");
                props.setProperty("jdbc.password", "");
                System.out.println("Using default database properties:");
                System.out.println("JDBC URL: " + props.getProperty("jdbc.url"));
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration file bd.config: " + e);
        }
        return props;
    }
}
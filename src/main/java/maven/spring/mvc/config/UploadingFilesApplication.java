package maven.spring.mvc.config;

import gate.CorpusController;
import gate.Gate;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import maven.spring.mvc.config.storage.StorageProperties;
import maven.spring.mvc.config.storage.StorageService;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

    public static void main(String[] args) throws GateException, IOException {

        SpringApplication.run(UploadingFilesApplication.class, args);
        Gate.init();


    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}

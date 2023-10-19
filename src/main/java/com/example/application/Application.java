package com.example.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@PWA(name = "My Progressive Web Application",
shortName = "MyPWA")
@SpringBootApplication
@Theme(value = "my-app")
public class Application implements AppShellConfigurator {

    public static Path imagesDirectory; 

    static {
        imagesDirectory = Paths.get("D:/Images");
        try {
            if (!Files.exists(imagesDirectory)) {
                Files.createDirectories(imagesDirectory);
            }
            System.out.println("Images will be stored in: " + imagesDirectory.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagesDirectoryPath = imagesDirectory.toUri().toString();
        if (!imagesDirectoryPath.endsWith("/")) {
            imagesDirectoryPath += "/";
        }
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesDirectoryPath);
    }


}

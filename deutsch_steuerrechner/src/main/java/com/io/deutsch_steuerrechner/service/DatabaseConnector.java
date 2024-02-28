package com.io.deutsch_steuerrechner.service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Configuration
@Data
public class DatabaseConnector {

    @Value("${spring.datasource.url}")
    public String datasourceUrl;

    @Value("${spring.datasource.username}")
    public String datasourceUsername;

    @Value("${spring.datasource.password}")
    public String datasourcePass;

}


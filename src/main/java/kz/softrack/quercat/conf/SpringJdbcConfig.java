package kz.softrack.quercat.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class SpringJdbcConfig {

    @Value("${quercat.db.url}")
    private String url;

    @Value("${quercat.db.login}")
    private String login;

    @Value("${quercat.db.pass}")
    private String pass;

    @Value("${quercat.db.driver}")
    private String driver;

    @Bean
    public DataSource mysqlDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(login);
        dataSource.setPassword(pass);

        return dataSource;
    }

}
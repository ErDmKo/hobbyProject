package tk.erdmko;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
@Import({ SecurityConfig.class })
public class Application extends SpringBootServletInitializer {
    private @Value("${spring.datasource.url}") String jdbcUrl;
    private @Value("${spring.datasource.username}") String username;
    private @Value("${spring.datasource.password}") String password;

    protected SpringApplicationBuilder configuration(SpringApplicationBuilder app) {
        return app.sources(Application.class);
    }
    public static void main(String[] args){
        ApplicationContext context = SpringApplication
                .run(Application.class, args);

    }
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);
        return filter;
    }
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(jdbcUrl, username, password);
    }

    @Bean
    public SessionFactory sessionFactory() {
        AnnotationSessionFactoryBean asFactoryBean =
                new AnnotationSessionFactoryBean();
        asFactoryBean.setDataSource(dataSource());
        // additional config
        return asFactoryBean.getObject();
    }
}

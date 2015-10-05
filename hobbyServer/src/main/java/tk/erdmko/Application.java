package tk.erdmko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    protected SpringApplicationBuilder configuration(SpringApplicationBuilder app) {
        return app.sources(Application.class);
    }
    public static void main(String[] args){
        ApplicationContext context = SpringApplication
                .run(Application.class, args);

    }
}

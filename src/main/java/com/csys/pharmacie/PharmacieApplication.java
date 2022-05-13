package com.csys.pharmacie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;

@SpringBootApplication(exclude = {SessionAutoConfiguration.class})
@EnableConfigurationProperties({LiquibaseProperties.class})
@EnableCircuitBreaker
@RefreshScope
@EnableAsync
public class PharmacieApplication {

    private static final Logger log = LoggerFactory.getLogger(PharmacieApplication.class);
    @Autowired
    private ApplicationContext context;
    // @Value("${default-lang}")
    // private static String defaultLang;

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(PharmacieApplication.class);
        // DefaultProfileUtil.addDefaultProfile(app);
        System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
        Environment env = app.run(args).getEnvironment();

        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        // defaultLang= env.getProperty("default-lang");
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application's name '{}' is running! Access URLs:\n\t" + "Local: \t\t{}://localhost:{}\n\t"
                        + "External: \t{}://{}:{}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), protocol, env.getProperty("server.port"), protocol,
                InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), env.getActiveProfiles());
    }


    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("DepstoQuerying-");
        executor.initialize();
        return executor;
    }
}

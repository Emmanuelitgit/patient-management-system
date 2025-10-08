package patient_management_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties
@EnableWebSecurity
@EnableScheduling
@EnableCaching
@EnableMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
public class Config {

    private final JwtAuthConverter jwtAuthConverter;
    @Value("${EMAIL_USERNAME}")
    private String EMAIL_USERNAME;
    @Value("${EMAIL_PASSWORD}")
    private String EMAIL_PASSWORD;

    @Autowired
    public Config(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests((auth)->{
            auth.requestMatchers("/swagger-ui/*");
            auth.anyRequest().permitAll();
        })
                .csrf((AbstractHttpConfigurer::disable))
                .cors((AbstractHttpConfigurer::disable))
//                .oauth2ResourceServer((auth->{
//                    auth.jwt(jwt->{
//                        jwt.jwtAuthenticationConverter(jwtAuthConverter);
//                    });
//                }))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(EMAIL_USERNAME);
        mailSender.setPassword(EMAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("authenticatedUserId");
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

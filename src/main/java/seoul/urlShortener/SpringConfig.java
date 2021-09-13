package seoul.urlShortener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seoul.urlShortener.repository.UrlRepository;
import seoul.urlShortener.repository.UrlRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UrlRepository urlRepository() {
        return new UrlRepositoryImpl(dataSource);
    }
}

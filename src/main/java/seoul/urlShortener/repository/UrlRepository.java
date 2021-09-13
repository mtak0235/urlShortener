package seoul.urlShortener.repository;

import seoul.urlShortener.domain.Url;
import java.util.Optional;

public interface UrlRepository {
    Url save(Url url) throws Exception;

    Optional<Url> findById(Long id);
    Optional<Url> findByLongurl(String longUrl);
}

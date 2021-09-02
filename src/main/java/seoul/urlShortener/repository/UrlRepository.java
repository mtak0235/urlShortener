package seoul.urlShortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.urlShortener.domain.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    String findLongurlById(int id);

    Long findIdByLongurl(String longurl);
}

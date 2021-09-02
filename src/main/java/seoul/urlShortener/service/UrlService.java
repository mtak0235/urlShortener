package seoul.urlShortener.service;

public interface UrlService {
    String generateShortUrl(String longurl);

    String getLongUrlByShortUrl(String shorturl);
}

package seoul.urlShortener.service;

public interface UrlService {
    String generateShortUrl(String longurl) throws Exception;

    String getLongUrlByShortUrl(String shorturl);
}

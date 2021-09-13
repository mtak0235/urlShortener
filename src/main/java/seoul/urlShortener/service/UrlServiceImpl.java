package seoul.urlShortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoul.urlShortener.domain.Url;
import seoul.urlShortener.repository.UrlRepository;
import seoul.urlShortener.utils.Base62Util;
import seoul.urlShortener.utils.UrlTypeValidation;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlTypeValidation urlTypeValidation;

    @Autowired
    private Base62Util base62Util;

    public Url updateLongUrl(String longurl)
    {
        Url url = new Url();
        url.setLongurl(longurl);
        return url;
    }

    @Override
    public String generateShortUrl(String longurl) throws Exception {

        if(!urlTypeValidation.valid(longurl)){
            throw new IllegalArgumentException("잘못된 URL 타입입니다.");
        }
        Url url;
        longurl = longurl.replace("https://","").replace("http://","");
        String shorturl;
        if (urlRepository.findByLongurl(longurl).isPresent()) {
            url = urlRepository.findByLongurl(longurl).get();
            shorturl = base62Util.encoding(url.getId().intValue());
            return "http://localhost:8080/"+ shorturl;
        }
        url = new Url();
        url.setLongurl(longurl);
        System.out.println("longurl = " + longurl);
        Url newUrl = urlRepository.save(url);
        Long id = newUrl.getId();
        shorturl = base62Util.encoding(id.intValue());
        return "http://localhost:8080/"+shorturl;
    }

    @Override
    public String getLongUrlByShortUrl(String shorturl) {
        Integer id = base62Util.decoding(shorturl);
        Long longId = Long.valueOf(id);
        Optional<Url> url;
        if (urlRepository.findById(longId).isPresent())
            url = urlRepository.findById(longId);
        else
            url = Optional.of(new Url());
        String longurl = url.get().getLongurl();
        return longurl;
    }
}

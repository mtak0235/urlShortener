package seoul.urlShortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoul.urlShortener.domain.Url;
import seoul.urlShortener.repository.UrlRepository;
import seoul.urlShortener.utils.Base62Util;
import seoul.urlShortener.utils.UrlTypeValidation;

import javax.transaction.Transactional;
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
    public String generateShortUrl(String longurl){

        if(!urlTypeValidation.valid(longurl)){
            throw new IllegalArgumentException("잘못된 URL 타입입니다.");
        }
        Url url = new Url();
        longurl = longurl.replace("https://","").replace("http://","");
        url.setLongurl(longurl);
        Url newUrl = urlRepository.saveAndFlush(url);
        Long id = newUrl.getId() + 20000l;
        String shorturl;
        if(id == null) {
            urlRepository.save(updateLongUrl(longurl));
            id = urlRepository.findIdByLongurl(longurl);
        }
        shorturl = base62Util.encoding(id.intValue());
        return "http://localhost:8080/"+shorturl;
    }

    @Override
    @Transactional
    public String getLongUrlByShortUrl(String shorturl) {
        Integer id = base62Util.decoding(shorturl) - 20000;
        Long longId = Long.valueOf(id);
        Optional<Url> url = urlRepository.findById(longId);
        String longurl = url.get().getLongurl();
        return longurl;
    }
}

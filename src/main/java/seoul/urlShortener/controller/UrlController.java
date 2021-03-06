package seoul.urlShortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.urlShortener.service.UrlService;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UrlController {
    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/")
    public String Shortening(@RequestParam String longurl, Model model) throws Exception {
        model.addAttribute("longurl",longurl);
        model.addAttribute("shorturl",urlService.generateShortUrl(longurl));
        return "index";
    }

    @GetMapping("/{shorturl}")
    public String redirect(HttpServletResponse response, @PathVariable String shorturl) {
        String longurl = urlService.getLongUrlByShortUrl(shorturl.replace("http://localhost:8080/", ""));
        if (longurl != null) {
            return "redirect:" + "http://" + longurl;
        }
        return "wrong_shortening";
    }
}

package QWESS.BACK.url;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final UrlRepository urlRepository;

    @GetMapping("/")
    public String home() {
        return "home/home";
    }

    @GetMapping("/scan-qr-url/nondb")
    public String scanQrUrlNondb(@RequestParam("scanUrl") String scanUrl, Model model) {
        Url url = new Url();
        url.setUrl(scanUrl);
        //db에는 아직 저장하지 않음
        model.addAttribute("url",url);
        return "home/scan-qr-url";
    }

    @GetMapping("/scan-qr-url")
    public String scanQrUrl(@RequestParam("scanUrl") String scanUrl, Model model) {
        Url url = new Url();
        url.setUrl(scanUrl);

        //db
        Optional<Boolean> tf = urlService.findByUrl(url.getUrl());
        if (tf.isEmpty()) {
            model.addAttribute("message", "머신러닝 서버로 악성 여부를 분석합니다.");

            //flask로 요청 전달
            Map<String, Object> result = urlService.mlServer(url.getUrl());
            Double phishing_probability = (Double) result.get("phishing_probability");
            Double legitimate_probability = (Double) result.get("legitimate_probability");


            if( phishing_probability > legitimate_probability) {
                String prediction = "⚠악성 URL로 판별되었습니다!";
                model.addAttribute("prediction",prediction);
                model.addAttribute("probability",phishing_probability);
                model.addAttribute("url",url);
                return "home/scan-qr-url-ml-result";
            } else {
                String prediction = "안전한 URL로 판별되었습니다!";
                model.addAttribute("prediction",prediction);
                model.addAttribute("probability",legitimate_probability);
                model.addAttribute("url",url);
                return "home/scan-qr-url-ml-result";

            }

        } else if (Boolean.TRUE.equals(tf.get())) {
            model.addAttribute("message", "안전한 URL입니다.");
            model.addAttribute("url",url);
            return "home/scan-qr-url-safe";
        } else {
            model.addAttribute("message", "악성 URL입니다.");
            model.addAttribute("url",url);
            return "home/scan-qr-url-virus";
        }
    }

    @GetMapping("/ml-server")
    public String mlServer() {
        try {
            String response = urlService.mlServerTest();
            System.out.println(response);
            return "home/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "home/home" ;
        }

    }


}

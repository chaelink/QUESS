package QWESS.BACK.url;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
//@Transactional
@RequiredArgsConstructor
public class UrlService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UrlRepository urlRepository;

    public void save(Url url) {
        urlRepository.save(url);
    }

    public void delete(Url url) {
        urlRepository.delete(url);
    }

    public Optional<Boolean> findByUrl(String url) {
        return urlRepository.findByUrl(url);
    }

    public Map<String, Object> mlServer(String url) {
        String flaskUrl = "http://52.78.98.235:8888/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"url\":\"" + url + "\"}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            // 머신러닝 서버에 HTTP POST 요청
            ResponseEntity<Map> response = restTemplate.exchange(
                    flaskUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );
            return response.getBody();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            // 에러 상황에서도 Map 형태로 반환
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("url", "Error");
            errorResult.put("accuracy", 0);
            return errorResult;
        }
    }

    public String mlServerTest() {
        String flaskUrl = "http://52.78.98.235:8888/testpage";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskUrl,
                    HttpMethod.GET,
                    request,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "Error: ML Server Unreachable";
        }
    }

}

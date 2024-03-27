package ru.crpt.ismp.crptapi;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@RestController
public class DocumentController {
    private final CrptApi crptApi;

    DocumentController() {
        crptApi = new CrptApi(new CrptApi.RequestLimiter(Duration.ofSeconds(10), 2));
    }

    @PostMapping(value = "/api/v3/lk/documents/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> createDocument(@RequestBody CrptApi.Document document) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.setContentDispositionFormData("attachment", "document.csv");
        byte[] bytes = crptApi.createDocument(document).getBytes(StandardCharsets.UTF_8);
        return new ResponseEntity<>(
                bytes,
                headers,
                HttpStatus.CREATED);
    }
}

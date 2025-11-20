package com.citiustech.HospitalManagement.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> home() {
        String html = """
                <!DOCTYPE html>
                <html lang=\"en\">
                <head>
                    <meta charset=\"UTF-8\" />
                    <title>Hospital Management API</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; }
                        a { color: #0b6efd; text-decoration: none; }
                        a:hover { text-decoration: underline; }
                    </style>
                </head>
                <body>
                    <h1>Hospital Management API</h1>
                    <p>This is the backend service for the Hospital Management system.</p>
                    <p>
                        Open <a href=\"/swagger-ui/index.html\">Swagger UI</a> to explore and test the REST APIs.
                    </p>
                </body>
                </html>
                """;
        return ResponseEntity.ok(html);
    }
}

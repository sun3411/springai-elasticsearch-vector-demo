package com.example.springai_elasticsearch_vector_demo.controller;

import com.example.springai_elasticsearch_vector_demo.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AIController {

    @Autowired
    AIService aiService;

    @GetMapping("/load")
    public String loadDocuments() {
        aiService.getDocuments();
        return "Documents loaded";
    }

    @GetMapping("/search")
    public String searchDocuments(@RequestParam(value = "query", defaultValue = "The world is big") String query) {
        String results = aiService.searchDocuments(query);
        return results;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam String filePath) {
        aiService.ingestPdf(filePath);
        return ResponseEntity.ok("处理完成");
    }
}

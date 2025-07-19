package com.example.springai_elasticsearch_vector_demo.service;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author : GoodLuck
 * @date 2025/7/10$ 13:21
 */


@Component
public class OllamaClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String OLLAMA_URL = "http://localhost:11434/api/chat";

    public String chat(String model, String userMessage) {
        // 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("stream", false); // 关键修改 ✅

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_URL, request, Map.class);

        // 解析返回内容
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object msg = response.getBody().get("message");
            if (msg instanceof Map) {
                Object content = ((Map<?, ?>) msg).get("content");

                return content != null ? content.toString() : "无回复内容";
            }
        }
        return "Ollama调用失败";
    }
}

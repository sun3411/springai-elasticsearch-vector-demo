package com.example.springai_elasticsearch_vector_demo.controller;

import com.example.springai_elasticsearch_vector_demo.service.LegalAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/legal")
public class LegalAgentController {

    @Autowired
    private LegalAgentService legalAgentService;

    /**
     * 智能法律查询
     */
    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> legalQuery(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");
            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "问题不能为空"));
            }

            String answer = legalAgentService.legalQuery(question);
            
            Map<String, Object> response = new HashMap<>();
            response.put("question", question);
            response.put("answer", answer);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "查询失败：" + e.getMessage()));
        }
    }

    /**
     * 统计分析
     */
    @PostMapping("/analysis")
    public ResponseEntity<Map<String, Object>> statisticalAnalysis(@RequestBody Map<String, String> request) {
        try {
            String analysisType = request.get("analysisType");
            if (analysisType == null || analysisType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "分析类型不能为空"));
            }

            String result = legalAgentService.statisticalAnalysis(analysisType);
            
            Map<String, Object> response = new HashMap<>();
            response.put("analysisType", analysisType);
            response.put("result", result);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "分析失败：" + e.getMessage()));
        }
    }

    /**
     * 案例对比
     */
    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> caseComparison(@RequestBody Map<String, String> request) {
        try {
            String case1 = request.get("case1");
            String case2 = request.get("case2");
            
            if (case1 == null || case2 == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "两个案例信息都不能为空"));
            }

            String result = legalAgentService.caseComparison(case1, case2);
            
            Map<String, Object> response = new HashMap<>();
            response.put("case1", case1);
            response.put("case2", case2);
            response.put("comparison", result);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "对比分析失败：" + e.getMessage()));
        }
    }

    /**
     * 法律风险评估
     */
    @PostMapping("/risk-assessment")
    public ResponseEntity<Map<String, Object>> legalRiskAssessment(@RequestBody Map<String, String> request) {
        try {
            String businessType = request.get("businessType");
            if (businessType == null || businessType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "行业类型不能为空"));
            }

            String result = legalAgentService.legalRiskAssessment(businessType);
            
            Map<String, Object> response = new HashMap<>();
            response.put("businessType", businessType);
            response.put("riskAssessment", result);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "风险评估失败：" + e.getMessage()));
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "Legal Agent Service");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
} 
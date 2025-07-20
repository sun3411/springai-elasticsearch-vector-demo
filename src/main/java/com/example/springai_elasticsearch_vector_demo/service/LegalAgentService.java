package com.example.springai_elasticsearch_vector_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LegalAgentService {

    private static final Logger logger = LoggerFactory.getLogger(LegalAgentService.class);

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private OllamaClient ollamaClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Value("${spring.ai.vectorstore.elasticsearch.index-name:lays}")
    private String indexName;

    /**
     * 智能法律查询Agent
     */
    public String legalQuery(String userQuestion) {
        try {
            // 1. 构建专业查询提示
            String enhancedQuery = buildLegalQuery(userQuestion);
            
            // 2. 向量检索相关案例
            List<Document> relevantCases = vectorStore.similaritySearch(
                SearchRequest.query(enhancedQuery)
                    .withTopK(5)
                    .withSimilarityThreshold(0.6)
            );

            // 3. 格式化检索结果
            String formattedResults = formatLegalResults(relevantCases);
            
            // 4. 构建专业回答
            String legalAnswer = generateLegalAnswer(userQuestion, formattedResults);
            
            return legalAnswer;
            
        } catch (Exception e) {
            logger.error("法律查询失败", e);
            return "抱歉，查询过程中出现错误，请稍后重试。";
        }
    }

    /**
     * 构建法律专业查询
     */
    private String buildLegalQuery(String userQuestion) {
        return String.format("""
            行政处罚案件查询：%s
            请检索相关的行政处罚案例，包括：
            - 违法行为类型
            - 处罚金额
            - 处罚依据
            - 案件处理部门
            - 处罚决定日期
            """, userQuestion);
    }

    /**
     * 格式化法律检索结果
     */
    private String formatLegalResults(List<Document> documents) {
        if (documents.isEmpty()) {
            return "未找到相关案例";
        }

        StringBuilder result = new StringBuilder();
        result.append("找到以下相关案例：\n\n");

        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            result.append(String.format("案例 %d:\n", i + 1));
            result.append(doc.getContent()).append("\n\n");
        }

        return result.toString();
    }

    /**
     * 生成专业法律回答
     */
    private String generateLegalAnswer(String question, String caseResults) {
        String prompt = String.format("""
            你是一位专业的法律顾问，专门处理行政处罚案件咨询。
            
            用户问题：%s
            
            相关案例信息：
            %s
            
            请基于以上案例信息，为用户提供专业的法律分析和建议：
            1. 分析相关案例的处罚情况
            2. 总结处罚规律和特点
            3. 提供法律建议和注意事项
            4. 如果涉及具体金额，请说明处罚标准
            
            请用中文回答，保持专业性和准确性。
            """, question, caseResults);

        return ollamaClient.chat("qwen2.5:7b", prompt);
    }

    /**
     * 统计分析查询
     */
    public String statisticalAnalysis(String analysisType) {
        String prompt = String.format("""
            请分析行政处罚案件数据，重点关注：
            %s
            
            请提供以下分析：
            1. 数据统计概览
            2. 主要违法类型分布
            3. 处罚金额分布
            4. 处理部门分布
            5. 时间趋势分析
            
            请用中文回答，提供具体的数据分析结果。
            """, analysisType);

        return ollamaClient.chat("qwen2.5:7b", prompt);
    }

    /**
     * 案例对比分析
     */
    public String caseComparison(String case1, String case2) {
        String prompt = String.format("""
            请对比分析以下两个行政处罚案例：
            
            案例1：%s
            案例2：%s
            
            请从以下角度进行对比分析：
            1. 违法行为类型对比
            2. 处罚金额对比
            3. 处罚依据对比
            4. 处理程序对比
            5. 相似性和差异性总结
            
            请用中文回答，提供专业的对比分析。
            """, case1, case2);

        return ollamaClient.chat("qwen2.5:7b", prompt);
    }

    /**
     * 法律风险评估
     */
    public String legalRiskAssessment(String businessType) {
        String prompt = String.format("""
            请为以下行业类型进行行政处罚法律风险评估：
            行业：%s
            
            请分析：
            1. 该行业常见的违法行为
            2. 主要处罚类型和金额
            3. 法律风险点识别
            4. 合规建议和预防措施
            5. 典型案例分析
            
            请用中文回答，提供实用的风险评估和建议。
            """, businessType);

        return ollamaClient.chat("qwen2.5:7b", prompt);
    }
} 
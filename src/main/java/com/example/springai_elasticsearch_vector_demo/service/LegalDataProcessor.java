package com.example.springai_elasticsearch_vector_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegalDataProcessor {

    private static final Logger logger = LoggerFactory.getLogger(LegalDataProcessor.class);

    @Autowired
    private VectorStore vectorStore;

    /**
     * 处理法律案件数据并存储到向量数据库
     */
    public void processLegalData(List<LegalCase> legalCases) {
        try {
            logger.info("开始处理 {} 个法律案件数据", legalCases.size());
            
            List<Document> documents = new ArrayList<>();
            
            for (LegalCase legalCase : legalCases) {
                // 构建案件描述文本
                String caseDescription = buildCaseDescription(legalCase);
                Document document = new Document(caseDescription);
                documents.add(document);
            }
            
            // 文本分块
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = new ArrayList<>();
            
            for (Document doc : documents) {
                List<Document> splits = textSplitter.split(doc);
                splitDocuments.addAll(splits);
            }
            
            // 存储到向量数据库
            vectorStore.add(splitDocuments);
            
            logger.info("成功处理并存储 {} 个法律案件文档", splitDocuments.size());
            
        } catch (Exception e) {
            logger.error("处理法律数据失败", e);
            throw new RuntimeException("法律数据处理失败", e);
        }
    }

    /**
     * 构建案件描述文本
     */
    private String buildCaseDescription(LegalCase legalCase) {
        StringBuilder description = new StringBuilder();
        
        description.append("行政处罚案件信息：\n");
        description.append("案件编号：").append(legalCase.getCaseId()).append("\n");
        description.append("违法行为：").append(legalCase.getViolation()).append("\n");
        description.append("处罚类型：").append(legalCase.getPenaltyType()).append("\n");
        description.append("处罚内容：").append(legalCase.getPenaltyContent()).append("\n");
        description.append("处罚金额：").append(legalCase.getPenaltySumRmbSum()).append(" 元\n");
        description.append("违法收入：").append(legalCase.getLawlessIncomeRmbSum()).append(" 元\n");
        description.append("处理部门：").append(legalCase.getRecordDept()).append("\n");
        description.append("处理人员：").append(legalCase.getRecordPpl()).append("\n");
        description.append("决定日期：").append(legalCase.getDecisionDate()).append("\n");
        description.append("立案日期：").append(legalCase.getRecordDate()).append("\n");
        description.append("案件来源：").append(legalCase.getSource()).append("\n");
        description.append("处罚类别：").append(legalCase.getPunishCategory()).append("\n");
        description.append("案件事实：").append(legalCase.getCaseLawlessFact()).append("\n");
        
        if (legalCase.getBusinessId() != null) {
            description.append("企业ID：").append(legalCase.getBusinessId()).append("\n");
        }
        
        if (legalCase.getCreditCode() != null) {
            description.append("统一社会信用代码：").append(legalCase.getCreditCode()).append("\n");
        }
        
        return description.toString();
    }

    /**
     * 法律案件数据模型
     */
    public static class LegalCase {
        private Integer caseId;
        private String violation;
        private String penaltyType;
        private String penaltyContent;
        private Double penaltySumRmbSum;
        private Double lawlessIncomeRmbSum;
        private String recordDept;
        private String recordPpl;
        private String decisionDate;
        private String recordDate;
        private String source;
        private String punishCategory;
        private String caseLawlessFact;
        private String businessId;
        private String creditCode;
        private String causeName;
        private String disName;
        private String documentNo;
        private String filingNo;
        private String funobeyby;
        private String hearDate;
        private String id;
        private String isDocument;
        private String penalty;
        private String processCounty;
        private String serialNumber;
        private String spePriName;
        private String urlOld;

        // Getters and Setters
        public Integer getCaseId() { return caseId; }
        public void setCaseId(Integer caseId) { this.caseId = caseId; }

        public String getViolation() { return violation; }
        public void setViolation(String violation) { this.violation = violation; }

        public String getPenaltyType() { return penaltyType; }
        public void setPenaltyType(String penaltyType) { this.penaltyType = penaltyType; }

        public String getPenaltyContent() { return penaltyContent; }
        public void setPenaltyContent(String penaltyContent) { this.penaltyContent = penaltyContent; }

        public Double getPenaltySumRmbSum() { return penaltySumRmbSum; }
        public void setPenaltySumRmbSum(Double penaltySumRmbSum) { this.penaltySumRmbSum = penaltySumRmbSum; }

        public Double getLawlessIncomeRmbSum() { return lawlessIncomeRmbSum; }
        public void setLawlessIncomeRmbSum(Double lawlessIncomeRmbSum) { this.lawlessIncomeRmbSum = lawlessIncomeRmbSum; }

        public String getRecordDept() { return recordDept; }
        public void setRecordDept(String recordDept) { this.recordDept = recordDept; }

        public String getRecordPpl() { return recordPpl; }
        public void setRecordPpl(String recordPpl) { this.recordPpl = recordPpl; }

        public String getDecisionDate() { return decisionDate; }
        public void setDecisionDate(String decisionDate) { this.decisionDate = decisionDate; }

        public String getRecordDate() { return recordDate; }
        public void setRecordDate(String recordDate) { this.recordDate = recordDate; }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public String getPunishCategory() { return punishCategory; }
        public void setPunishCategory(String punishCategory) { this.punishCategory = punishCategory; }

        public String getCaseLawlessFact() { return caseLawlessFact; }
        public void setCaseLawlessFact(String caseLawlessFact) { this.caseLawlessFact = caseLawlessFact; }

        public String getBusinessId() { return businessId; }
        public void setBusinessId(String businessId) { this.businessId = businessId; }

        public String getCreditCode() { return creditCode; }
        public void setCreditCode(String creditCode) { this.creditCode = creditCode; }

        public String getCauseName() { return causeName; }
        public void setCauseName(String causeName) { this.causeName = causeName; }

        public String getDisName() { return disName; }
        public void setDisName(String disName) { this.disName = disName; }

        public String getDocumentNo() { return documentNo; }
        public void setDocumentNo(String documentNo) { this.documentNo = documentNo; }

        public String getFilingNo() { return filingNo; }
        public void setFilingNo(String filingNo) { this.filingNo = filingNo; }

        public String getFunobeyby() { return funobeyby; }
        public void setFunobeyby(String funobeyby) { this.funobeyby = funobeyby; }

        public String getHearDate() { return hearDate; }
        public void setHearDate(String hearDate) { this.hearDate = hearDate; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getIsDocument() { return isDocument; }
        public void setIsDocument(String isDocument) { this.isDocument = isDocument; }

        public String getPenalty() { return penalty; }
        public void setPenalty(String penalty) { this.penalty = penalty; }

        public String getProcessCounty() { return processCounty; }
        public void setProcessCounty(String processCounty) { this.processCounty = processCounty; }

        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

        public String getSpePriName() { return spePriName; }
        public void setSpePriName(String spePriName) { this.spePriName = spePriName; }

        public String getUrlOld() { return urlOld; }
        public void setUrlOld(String urlOld) { this.urlOld = urlOld; }
    }
} 
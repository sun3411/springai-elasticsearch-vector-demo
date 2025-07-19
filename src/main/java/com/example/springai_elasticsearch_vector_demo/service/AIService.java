package com.example.springai_elasticsearch_vector_demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private OllamaClient ollamaClient;

    @Autowired
    private EmbeddingModel embeddingModel;


    @Value("classpath:product.json")
    Resource resource;

    public void getDocuments() {
        logger.info("Loading products from {}", resource.getFilename());
        List<Document> documents = readAndPrintJsonFile();
        TextSplitter textSplitter = new TokenTextSplitter();
        documents.forEach(document -> {
            List<Document> splitDocuments = textSplitter.split(document);
            logger.info("Split document into {} parts", splitDocuments.size());
            vectorStore.add(splitDocuments);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Added document to vector store {}", document.toString());
        });
        logger.info("Finished loading products");
    }

    private List<Document> readAndPrintJsonFile() {
        List<Document> documents = new ArrayList<>();
        try(InputStream inputStream= resource.getInputStream()){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            for (JsonNode node : jsonNode) {
                if(node.has("description")) {
                    System.out.println(node.get("description").toString());
                    documents.add(new Document(node.get("description").toString()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    public void ingestPdf(String filePath) {
        try {
            // 1. 读取 PDF 内容
            FileSystemResource resource = new FileSystemResource(filePath);
            List<Document> documents = new PagePdfDocumentReader(resource).read();

            // 2. 分块（token-aware）
            List<Document> splitDocs = new TokenTextSplitter().apply(documents);

            // 3. 写入向量存储（自动生成 embedding）
            vectorStore.add(splitDocs);

            System.out.println("✅ PDF 处理完成，向量已写入 ES 向量索引！");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ 处理失败：" + e.getMessage());
        }
    }

    public String searchDocuments(String query) {
        List<Document> vectorStoreResult = vectorStore.similaritySearch(SearchRequest.query(query).withTopK(3).withSimilarityThreshold(0.7));
        String documents = vectorStoreResult.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        String prompt = """
                    你正在协助提供裁量基准的内容。
                    使用“文档”部分提供的信息来准确回答问题
                     问题部分的问题。
                    如果不确定，简单地说你不知道
                
                文档:
                """ + documents
                + """
                问题:
                """ + query;
        return chat("phi3", prompt);
    }

    public String chat(String model, String userMessage) {
        return ollamaClient.chat(model, userMessage);
    }
}

# Spring AI + Elasticsearch + Ollama RAG系统

基于Spring AI框架构建的RAG（检索增强生成）系统，使用Elasticsearch作为向量数据库，Ollama作为本地AI模型。

## 系统架构

- **Spring Boot 3.3.4**: 后端框架
- **Spring AI 1.0.0-M2**: AI集成框架
- **Elasticsearch**: 向量存储
- **Ollama**: 本地AI模型服务

## 环境要求

- Java 23
- Docker (用于Elasticsearch)
- Ollama (本地AI模型)

## 快速开始

### 1. 安装Ollama

#### macOS安装
```bash
# 下载并安装Ollama
curl -fsSL https://ollama.ai/install.sh | sh

# 启动Ollama服务
ollama serve
```

#### 下载模型
```bash
# 下载推荐的7B模型（适合MacBook）
ollama pull qwen2.5:7b

# 或者下载其他模型
ollama pull llama2:7b
ollama pull gemma2:9b
```

### 2. 启动Elasticsearch

```bash
# 进入elasticsearch目录
cd elasticsearch

# 启动Elasticsearch容器
docker-compose up -d
```

### 3. 配置应用

编辑 `src/main/resources/application.properties`:

```properties
# Ollama配置
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=qwen2.5:7b
spring.ai.ollama.embedding.model=qwen2.5:7b
```

### 4. 运行应用

```bash
# 编译并运行
mvn spring-boot:run
```

## API接口

### 基础功能

- `POST /api/documents/load` - 加载示例文档
- `POST /api/documents/add` - 添加新文档
- `GET /api/documents/search?query={query}` - 搜索文档

### 法律Agent功能

#### 智能法律查询
```bash
POST /api/legal/query
Content-Type: application/json

{
  "question": "查询关于环境污染的行政处罚案例"
}
```

#### 统计分析
```bash
POST /api/legal/analysis
Content-Type: application/json

{
  "analysisType": "处罚金额分布分析"
}
```

#### 案例对比
```bash
POST /api/legal/compare
Content-Type: application/json

{
  "case1": "案例1的详细信息",
  "case2": "案例2的详细信息"
}
```

#### 法律风险评估
```bash
POST /api/legal/risk-assessment
Content-Type: application/json

{
  "businessType": "制造业"
}
```

### 数据管理

- `POST /api/data/import-from-lays` - 从lays索引导入数据
- `POST /api/data/import-legal-cases` - 批量导入法律案件
- `GET /api/data/import-status` - 检查导入状态

## 法律Agent特性

### 🎯 专业功能

1. **智能法律查询**
   - 基于语义相似度的案例检索
   - 专业法律分析和建议
   - 处罚规律总结

2. **统计分析**
   - 处罚金额分布分析
   - 违法类型统计
   - 时间趋势分析
   - 部门处理情况统计

3. **案例对比**
   - 相似案例对比分析
   - 处罚标准对比
   - 处理程序对比

4. **风险评估**
   - 行业法律风险识别
   - 合规建议提供
   - 预防措施建议

### 📊 数据字段支持

支持以下法律案件字段的智能查询：
- `caseId`: 案件编号
- `violation`: 违法行为
- `penaltyType`: 处罚类型
- `penaltyContent`: 处罚内容
- `penaltySumRmbSum`: 处罚金额
- `lawlessIncomeRmbSum`: 违法收入
- `recordDept`: 处理部门
- `recordPpl`: 处理人员
- `decisionDate`: 决定日期
- `recordDate`: 立案日期
- `source`: 案件来源
- `punishCategory`: 处罚类别
- `caseLawlessFact`: 案件事实

## 性能优化建议

### MacBook优化

1. **模型选择**: 优先使用7B参数模型
2. **内存管理**: 确保有足够可用内存
3. **GPU加速**: M系列芯片自动启用Metal加速

### 系统调优

1. **向量维度**: 根据模型调整embedding维度
2. **分块大小**: 优化文档分块策略
3. **检索参数**: 调整相似度阈值和返回数量

## 故障排除

### Ollama常见问题

1. **模型下载失败**: 检查网络连接
2. **内存不足**: 选择更小的模型
3. **启动失败**: 检查端口11434是否被占用

### Elasticsearch问题

1. **连接失败**: 检查Docker容器状态
2. **认证错误**: 确认用户名密码配置
3. **索引创建失败**: 检查schema初始化设置

## 模型推荐

### 中文场景

- `qwen2.5:7b` - 阿里开源，中文支持优秀
- `chatglm3:6b` - 清华开源，中文对话能力强

### 英文场景

- `llama2:7b` - Meta开源，通用性能好
- `gemma2:9b` - Google开源，性能稳定

### 轻量级选择

- `phi3:mini` - 微软开源，3.8B参数
- `tinyllama:1.1b` - 超轻量级，适合资源受限环境

## 使用示例

### 法律查询示例

```bash
# 查询环境污染案例
curl -X POST http://localhost:8080/api/legal/query \
  -H "Content-Type: application/json" \
  -d '{"question": "查询关于环境污染的行政处罚案例，重点关注处罚金额和处理部门"}'

# 分析处罚金额分布
curl -X POST http://localhost:8080/api/legal/analysis \
  -H "Content-Type: application/json" \
  -d '{"analysisType": "处罚金额分布分析"}'

# 制造业风险评估
curl -X POST http://localhost:8080/api/legal/risk-assessment \
  -H "Content-Type: application/json" \
  -d '{"businessType": "制造业"}'
```

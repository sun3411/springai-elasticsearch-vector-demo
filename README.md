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

### 文档管理

- `POST /api/documents/load` - 加载示例文档
- `POST /api/documents/add` - 添加新文档
- `GET /api/documents/search?query={query}` - 搜索文档

### RAG问答

- `POST /api/chat/ask` - 智能问答

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

# AGENTS.md - Big Event CMS Development Guide

## Project Overview
Spring Boot 3.4.1 CMS backend with Java 23, JWT auth, Redis, MyBatis, Ollama AI, Elasticsearch.

## Build Commands
```bash
mvn clean compile -DskipTests
mvn test -Dtest=ArticleServiceTest
mvn test
mvn clean package -DskipTests
mvn clean test jacoco:report
mvn spring-boot:run
```

## Code Style

### Naming Conventions
- Classes: PascalCase (`UserController`, `ArticleServiceImpl`)
- Methods/Variables: camelCase (`findByUserName`, `articleList`)
- Constants: UPPER_SNAKE_CASE
- Tables: snake_case with `tb_` prefix
- DTOs: `*Request`, `*Response`, `*DTO` suffix

### Imports Order
```java
import java.util.*;
import com.cxx.bigevent.dto.*;
import com.cxx.bigevent.exception.*;
import com.cxx.bigevent.mapper.*;
import com.cxx.bigevent.pojo.*;
import com.cxx.bigevent.service.*;
import org.springframework.*;
import static org.springframework.util.StringUtils.*;
```

### Controller Pattern
```java
@RestController
@RequestMapping("/api/article")
@Tag(name = "文章管理", description = "文章的增删改查接口")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    @Autowired private ArticleService articleService;
    @PostMapping
    @Operation(summary = "创建文章", description = "创建一篇新文章")
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        log.info("文章添加成功：{}", article.getTitle());
        return Result.success();
    }
}
```

### Service Pattern
```java
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {
    @Autowired private ArticleMapper articleMapper;
    @Override
    public void add(Article article) { }
    @Override
    public void update(Article article, Integer id) {
        Article existing = articleMapper.findById(id, userId);
        if (existing == null) throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
```

### Mapper Pattern
- Use annotations for simple CRUD: `@Insert`, `@Select`, `@Update`, `@Delete`
- Use XML mapping for complex queries with `<foreach>`
- Example annotation mapper:
```java
@Mapper
public interface ArticleMapper {
    @Insert("INSERT INTO tb_article(...) VALUES(...)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Article article);
    @Select("SELECT * FROM tb_article WHERE id = #{id}")
    Article findById(@Param("id") Integer id);
}
```

### DTO/Request Pattern
```java
@Data
public class RegisterRequest {
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符")
    private String username;
    @Pattern(regexp = "^\\S{5,16}$", message = "密码必须是5-16位非空字符")
    private String password;
}
```

### Response/Error Handling
```java
return Result.success(data);
return Result.success();
return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
```

### ErrorCode Enum
```java
public enum ErrorCode {
    SUCCESS(0, "操作成功"),
    ERROR(1, "操作失败"),
    ARTICLE_NOT_FOUND(2003, "文章不存在");
}
```

### Validation & Documentation
- Use `@Validated` on controllers, Jakarta validation annotations on DTOs
- Use `@Tag` and `@Operation` for Swagger/OpenAPI documentation
- Log with `log.info()`, `log.warn()`, `log.error()` with context

### Custom Annotations
- `@State` - Custom validation for article/state values
- `@RequireRole` - Role-based access control
- `@RequirePermission` - Permission-based access control

## Project Structure
```
src/main/java/com/cxx/bigevent/
├── annotation/       # @RequireRole, @State, @RequirePermission
├── config/           # Configuration classes
├── controller/       # REST controllers (incl. admin/)
├── dto/              # Data transfer objects
├── exception/        # BusinessException, ErrorCode
├── mapper/           # MyBatis mappers
├── pojo/             # Entity classes, Result, PageBean
├── security/rbac/    # RBAC implementation
├── service/impl/     # Service implementations
└── validation/       # Custom validators
```

## Configuration
- `application.yml` - Main configuration with Redis, MySQL, Elasticsearch, Caffeine, Resilience4j
- i18n messages in `src/main/resources/i18n/messages*.properties`
- Environment variables: `DB_PASSWORD`, `REDIS_HOST`, `ALIYUN_OSS_*`, `OLLAMA_HOST`

## Common Issues
- **Lombok**: `mvn clean compile` + invalidate IDE caches
- **Redis**: Check server and `.env` settings
- **Ollama**: Run `ollama serve` and `ollama pull deepseek-r1:8b`
- **Timezone**: Configure `serverTimezone=Asia/Shanghai` in DB URL
- **MyBatis**: Use XML mapping for batch operations with foreach

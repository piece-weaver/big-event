# AGENTS.md - Big Event CMS Development Guide

## Project Overview
Spring Boot 3.4.1 CMS backend with Java 23, JWT auth, Redis, MyBatis, Ollama AI, Elasticsearch.

## Build Commands
```bash
mvn clean compile -DskipTests                    # Compile without tests
mvn test -Dtest=ArticleServiceTest              # Run single test class
mvn test                                        # Run all tests
mvn clean package -DskipTests                   # Package without tests
mvn spring-boot:run                             # Start application
mvn clean test jacoco:report                    # Generate test coverage (requires jacoco plugin)
```

## Testing Commands
```bash
mvn test                                        # Run all unit tests
mvn test -Dtest=ArticleServiceTest              # Run specific test class
mvn test -Dtest=*ServiceTest                    # Run all service tests
mvn clean test                                  # Clean and run all tests
```

## Code Style Guidelines

### Naming Conventions
- **Classes**: PascalCase (`UserController`, `ArticleServiceImpl`)
- **Methods/Variables**: camelCase (`findByUserName`, `articleList`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_RETRY_COUNT`)
- **Database Tables**: snake_case with `tb_` prefix (`tb_user`, `tb_article`)
- **Database Fields**: snake_case (`create_time`, `user_name`)
- **DTOs**: `*Request`, `*Response`, `*DTO` suffix (`LoginRequest`, `UserResponse`)
- **Enums**: PascalCase (`ErrorCode`, `ArticleState`)

### Import Order & Organization
```java
// 1. Java standard library
import java.time.LocalDateTime;
import java.util.*;

// 2. Jakarta EE
import jakarta.validation.constraints.*;

// 3. Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// 4. Third-party libraries
import com.auth0.jwt.JWT;
import io.swagger.v3.oas.annotations.*;

// 5. Project packages (alphabetical)
import com.cxx.bigevent.annotation.*;
import com.cxx.bigevent.config.*;
import com.cxx.bigevent.dto.*;
import com.cxx.bigevent.exception.*;
import com.cxx.bigevent.mapper.*;
import com.cxx.bigevent.pojo.*;
import com.cxx.bigevent.service.*;

// 6. Static imports (at end)
import static org.springframework.util.StringUtils.*;
```

### Controller Pattern
```java
@RestController
@RequestMapping("/api/article")
@Tag(name = "文章管理", description = "文章的增删改查接口")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @Operation(summary = "创建文章", description = "创建一篇新文章")
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        log.info("文章添加成功：{}", article.getTitle());
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章", description = "根据ID获取文章详情")
    public Result<Article> findById(@PathVariable Integer id) {
        Article article = articleService.findById(id);
        return Result.success(article);
    }
}
```

### Service Layer Pattern
```java
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void add(Article article) {
        // Business logic with validation
        articleMapper.add(article);
        eventPublisher.publishEvent(new ArticleCreateEvent(this, article));
    }

    @Override
    public Article findById(Integer id) {
        Article article = articleMapper.findById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        return article;
    }
}
```

### Data Access (Mapper) Pattern
```java
@Mapper
public interface ArticleMapper {
    // Simple CRUD with annotations
    @Insert("INSERT INTO tb_article(title, content, create_user, create_time) " +
            "VALUES(#{title}, #{content}, #{createUser}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Article article);

    @Select("SELECT * FROM tb_article WHERE id = #{id}")
    Article findById(@Param("id") Integer id);

    // Complex queries use XML mapping with <foreach> for batch operations
    List<Article> list(@Param("categoryId") Integer categoryId,
                      @Param("state") Integer state,
                      @Param("userId") Integer userId);
}
```

### DTO and Validation Pattern
```java
@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码必须是5-16位非空字符")
    private String password;
}

@Data
public class ArticleResponse {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

### Response and Error Handling
```java
// Success responses
return Result.success(data);                    // With data
return Result.success();                       // Without data

// Error responses
return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
throw new BusinessException(ErrorCode.USER_NOT_FOUND);

// Custom error with message
return Result.error("Custom error message");
```

### Error Code Enum Pattern
```java
public enum ErrorCode {
    // Success
    SUCCESS(0, "操作成功"),

    // General errors
    ERROR(1, "操作失败"),

    // User related (1000-1999)
    USER_USERNAME_EXISTS(1001, "用户名已存在"),
    USER_LOGIN_ERROR(1002, "用户名或密码错误"),

    // Article related (2000-2999)
    ARTICLE_NOT_FOUND(2003, "文章不存在"),

    // Authentication (7000-7999)
    UNAUTHORIZED(7005, "请先登录");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
```

### Logging Standards
```java
private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

// Info level for normal operations
log.info("文章添加成功：{}", article.getTitle());

// Warn level for potential issues
log.warn("用户尝试访问不存在的文章：{}", articleId);

// Error level for exceptions and failures
log.error("数据库操作失败", e);
```

### Security Best Practices
- **Never log sensitive data**: Passwords, tokens, secrets
- **Use ThreadLocalUtil** for user context instead of passing userId everywhere
- **Validate all inputs** with Jakarta validation annotations
- **Use @RequireRole/@RequirePermission** for authorization
- **Enable CORS** only for necessary origins in production

## Project Structure
```
src/main/java/com/cxx/bigevent/
├── annotation/           # Custom annotations (@RequireRole, @State)
├── config/              # Configuration classes (Redis, Cache, Security)
├── controller/          # REST controllers (Article, User, Admin)
├── dto/                 # Data transfer objects (Request/Response)
├── event/               # Spring events (ArticleCreateEvent)
├── exception/           # Custom exceptions (BusinessException)
├── health/              # Health indicators
├── interceptors/        # HTTP interceptors
├── mapper/              # MyBatis mappers
├── pojo/                # Entity classes (Article, User, Result)
├── scheduler/           # Scheduled tasks
├── security/            # Security configuration and RBAC
├── service/             # Service interfaces
│   └── impl/            # Service implementations
├── util/                # Utility classes (ThreadLocalUtil)
├── validation/          # Custom validators
└── BigEventApplication.java
```

## Configuration Files
- `application.yml` - Main config (DB, Redis, Cache, Resilience4j)
- `src/main/resources/i18n/messages*.properties` - Internationalization
- Environment variables: `DB_PASSWORD`, `REDIS_HOST`, `ALIYUN_OSS_*`, `OLLAMA_HOST`

## Common Development Issues
- **Lombok not working**: Run `mvn clean compile`, invalidate IDE caches
- **Redis connection**: Check `.env` settings, ensure Redis server running
- **Ollama AI**: Run `ollama serve && ollama pull deepseek-r1:8b`
- **MySQL timezone**: Set `serverTimezone=Asia/Shanghai` in connection URL
- **MyBatis batch ops**: Use XML mapping with `<foreach>` for complex queries
- **Validation not working**: Ensure `@Validated` on controllers, Jakarta annotations on DTOs

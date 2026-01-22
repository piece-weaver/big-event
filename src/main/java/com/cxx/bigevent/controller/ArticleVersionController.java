package com.cxx.bigevent.controller;

import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.ArticleVersionMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.ArticleVersion;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.util.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "文章版本", description = "文章版本历史管理接口")
public class ArticleVersionController {

    @Autowired
    private ArticleVersionMapper versionMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/{id}/versions")
    @Operation(summary = "获取版本历史", description = "获取指定文章的所有版本历史")
    public Result<List<ArticleVersion>> getVersions(@PathVariable Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Article article = articleMapper.findById(id, userId);
        if (article == null) {
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }

        List<ArticleVersion> versions = versionMapper.findByArticleId(id.longValue());
        return Result.success(versions);
    }

    @GetMapping("/{id}/versions/{versionId}")
    @Operation(summary = "获取版本详情", description = "获取指定文章的某个版本详情")
    public Result<ArticleVersion> getVersionDetail(@PathVariable Integer id, @PathVariable Integer versionId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Article article = articleMapper.findById(id, userId);
        if (article == null) {
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleVersion version = versionMapper.findByArticleIdAndVersion(id.longValue(), versionId);
        if (version == null) {
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }

        return Result.success(version);
    }

    @PostMapping("/{id}/versions/{versionId}/restore")
    @Operation(summary = "恢复版本", description = "将文章恢复到指定的历史版本")
    public Result<String> restoreVersion(@PathVariable Integer id, @PathVariable Integer versionId,
                                         @RequestParam(required = false) String changeSummary) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Article article = articleMapper.findById(id, userId);
        if (article == null) {
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleVersion version = versionMapper.findByArticleIdAndVersion(id.longValue(), versionId);
        if (version == null) {
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }

        Integer maxVersion = versionMapper.findMaxVersionNumber(id.longValue());
        int newVersionNumber = (maxVersion != null ? maxVersion : 0) + 1;

        ArticleVersion newVersion = new ArticleVersion();
        newVersion.setArticleId(id.longValue());
        newVersion.setTitle(article.getTitle());
        newVersion.setContent(article.getContent());
        newVersion.setCoverImg(article.getCoverImg());
        newVersion.setState(article.getState());
        newVersion.setCategoryId(article.getCategoryId().longValue());
        newVersion.setVersionNumber(newVersionNumber);
        newVersion.setChangeSummary("恢复到版本 " + versionId + (changeSummary != null ? ": " + changeSummary : ""));
        newVersion.setCreatedBy(userId.longValue());

        versionMapper.insert(newVersion);

        article.setTitle(version.getTitle());
        article.setContent(version.getContent());
        article.setCoverImg(version.getCoverImg());
        article.setState(version.getState());
        article.setCategoryId(version.getCategoryId().intValue());

        articleMapper.update(article, id);

        return Result.success("成功恢复到版本 " + versionId);
    }

    @GetMapping("/{id}/versions/latest")
    @Operation(summary = "获取最新版本", description = "获取指定文章的最新版本")
    public Result<ArticleVersion> getLatestVersion(@PathVariable Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Article article = articleMapper.findById(id, userId);
        if (article == null) {
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleVersion version = versionMapper.findLatestVersion(id.longValue());
        return Result.success(version);
    }
}

package com.cxx.bigevent.controller.admin;

import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.security.rbac.annotation.RequirePermission;
import com.cxx.bigevent.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/export")
@Tag(name = "数据导出", description = "文章数据导出管理接口")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/articles")
    @RequirePermission("article:export")
    @Operation(summary = "导出文章数据", description = "导出所有文章数据（JSON格式）")
    public Result<byte[]> exportArticles() {
        byte[] data = exportService.exportAllData();
        return Result.success(data);
    }

    @GetMapping("/articles/Excel")
    @RequirePermission("article:export")
    @Operation(summary = "导出文章Excel", description = "导出所有文章数据（Excel格式）")
    public Result<byte[]> exportArticlesExcel() {
        byte[] data = exportService.exportAllData();
        return Result.success(data);
    }
}

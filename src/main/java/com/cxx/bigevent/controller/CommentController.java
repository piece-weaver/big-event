package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.CommentDTO;
import com.cxx.bigevent.dto.CommentUpdateDTO;
import com.cxx.bigevent.pojo.Comment;
import com.cxx.bigevent.pojo.CommentVO;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.CommentService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@Validated
@Tag(name = "评论管理", description = "文章评论的增删改查接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @Operation(summary = "发表评论", description = "对文章发表评论或回复")
    public Result<Void> add(@RequestBody @Valid CommentDTO dto) {
        Comment comment = new Comment();
        comment.setArticleId(dto.getArticleId());
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId());

        commentService.add(comment);
        return Result.success();
    }

    @GetMapping("/article/{articleId}")
    @Operation(summary = "获取评论", description = "获取指定文章的所有评论")
    public Result<List<CommentVO>> findByArticleId(@PathVariable Long articleId) {
        List<CommentVO> comments = commentService.findByArticleId(articleId);
        return Result.success(comments);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新评论", description = "更新指定评论的内容")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid CommentUpdateDTO dto) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(dto.getContent());

        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();
        comment.setUserId(userId);

        commentService.update(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论", description = "删除指定评论")
    public Result<Void> delete(@PathVariable Long id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        commentService.delete(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞评论", description = "对指定评论点赞")
    public Result<Void> like(@PathVariable Long id) {
        commentService.likeComment(id);
        return Result.success();
    }
}

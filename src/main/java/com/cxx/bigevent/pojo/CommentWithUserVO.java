package com.cxx.bigevent.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CommentWithUserVO {
    private Long id;
    private Long articleId;
    private String content;
    private Long parentId;
    private Integer likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long userId;
    private String nickname;
    private String userPic;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getUserPic() { return userPic; }
    public void setUserPic(String userPic) { this.userPic = userPic; }

    public UserVO getUser() {
        UserVO userVO = new UserVO();
        userVO.setId(userId);
        userVO.setNickname(nickname);
        userVO.setUserPic(userPic);
        return userVO;
    }
}

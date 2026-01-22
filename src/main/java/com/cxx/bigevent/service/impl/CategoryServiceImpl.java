package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.CategoryMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;
import com.cxx.bigevent.service.CategoryService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        categoryMapper.add(category, userId);
    }

    @Override
    public List<Category> list() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return categoryMapper.list(userId);
    }

    @Override
    public Category findCategoryById(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return categoryMapper.findCategoryById(id, userId);
    }

    @Override
    public List<Article> findArticlesByCategoryId(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return categoryMapper.findArticlesByCategoryId(id, userId);
    }

    @Override
    public void update(Category category, Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        categoryMapper.update(category, id, userId);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        
        Category category = categoryMapper.findCategoryById(id, userId);
        if (category == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        
        int articleCount = articleMapper.countByCategoryId(id, userId);
        if (articleCount > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_ARTICLES);
        }
        
        int affectedRows = categoryMapper.delete(id, userId);
        
        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteWithArticles(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        
        Category category = categoryMapper.findCategoryById(id, userId);
        if (category == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        
        articleMapper.deleteByCategoryId(id, userId);
        
        int affectedRows = categoryMapper.delete(id, userId);
        
        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}

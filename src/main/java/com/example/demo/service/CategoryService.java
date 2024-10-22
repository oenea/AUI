package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }
    
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }
}
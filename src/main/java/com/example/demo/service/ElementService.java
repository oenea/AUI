package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Element;
import com.example.demo.repository.ElementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ElementService {
    private final ElementRepository elementRepository;
    
    public ElementService(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Element> findAll() {
        return elementRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Element> findByCategory(Category category) {
        return elementRepository.findByCategory(category);
    }
    
    @Transactional(readOnly = true)
    public Optional<Element> findById(UUID id) {
        return elementRepository.findById(id);
    }
    
    public Element save(Element element) {
        return elementRepository.save(element);
    }
    
    public void deleteById(UUID id) {
        elementRepository.deleteById(id);
    }
}
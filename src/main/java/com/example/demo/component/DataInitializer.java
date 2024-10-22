package com.example.demo.component;

import com.example.demo.model.Category;
import com.example.demo.model.Element;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ElementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {
    private final CategoryService categoryService;
    private final ElementService elementService;
    
    public DataInitializer(CategoryService categoryService, ElementService elementService) {
        this.categoryService = categoryService;
        this.elementService = elementService;
    }
    
    @Override
    @Transactional
    public void run(String... args) {
        initializeData();
    }

    private void initializeData() {
        Category CPUs = createCategory("CPU", "this thing that is socketed into motherboard");
        Category Motherboards = createCategory("Motherboards", "connect all PC components");
        Category PSUs = createCategory("PSUs", "power all PC components");

        createElement("Ryzen 5 2600", "6C/12T", CPUs);
        createElement("Gigabyte B450", "powerful mb", Motherboards);
        createElement("Akyga 300W", "80+", PSUs);

        createElement("Ryzen 3 3100", "4C/8T", CPUs);
        createElement("Ryzen 9 9950x", "16C/32T", CPUs);
        createElement("Intel Core Ultra 9 285K", "24C/24T", CPUs);

        createElement("Corsair 500W", "80+ Gold", PSUs);
        createElement("Seasonic 600W", "80+ Gold", PSUs);
    }

    private Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryService.save(category);
    }

    private Element createElement(String name, String description, Category category) {
        Element element = new Element();
        element.setName(name);
        element.setDescription(description);
        element.setCategory(category);
        
        category.getElements().add(element);
        
        Element savedElement = elementService.save(element);
        
        categoryService.save(category);
        
        return savedElement;
    }
}
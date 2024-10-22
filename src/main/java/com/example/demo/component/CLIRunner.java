package com.example.demo.component;

import com.example.demo.model.Category;
import com.example.demo.model.Element;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ElementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@Component
@Order(2)
public class CLIRunner implements CommandLineRunner {
    private final CategoryService categoryService;
    private final ElementService elementService;
    private final ConfigurableApplicationContext context;
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public CLIRunner(CategoryService categoryService, ElementService elementService,
                    ConfigurableApplicationContext context) {
        this.categoryService = categoryService;
        this.elementService = elementService;
        this.context = context;
    }

    @Override
    public void run(String... args) {
        System.out.println("\nWelcome to the Element Management System!");
        while (running) {
            try {
                printMenu();
                String choice = scanner.nextLine().trim();
                processChoice(choice);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\nAvailable commands:");
        System.out.println("1. List all categories");
        System.out.println("2. List all elements");
        System.out.println("3. Add new element");
        System.out.println("4. Delete element");
        System.out.println("5. Exit");
        System.out.println("Enter your choice: ");
    }

    private void processChoice(String choice) {
        switch (choice) {
            case "1" -> listCategories();
            case "2" -> listElements();
            case "3" -> addElement();
            case "4" -> deleteElement();
            case "5" -> exit();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void listCategories() {
        System.out.println("\nCategories:");
        var categories = categoryService.findAll();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }
        categories.forEach(category ->
            System.out.println(category.getId() + ": " + category.getName() + 
                             " (" + category.getDescription() + ")"));
    }

    private void listElements() {
        System.out.println("\nElements:");
        var elements = elementService.findAll();
        if (elements.isEmpty()) {
            System.out.println("No elements found.");
            return;
        }
        elements.forEach(element ->
            System.out.println(element.getId() + ": " + element.getName() +
                             " (Category: " + element.getCategory().getName() + ")"));
    }

    @Transactional
    private void addElement() {
        try {
            System.out.println("\nAvailable categories:");
            var categories = categoryService.findAll();
            if (categories.isEmpty()) {
                System.out.println("No categories available.");
                return;
            }
            categories.forEach(category ->
                System.out.println(category.getId() + ": " + category.getName()));

            System.out.print("\nEnter category ID: ");
            String categoryIdStr = scanner.nextLine().trim();
            
            UUID categoryId;
            try {
                categoryId = UUID.fromString(categoryIdStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format.");
                return;
            }

            Optional<Category> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                System.out.println("Category not found.");
                return;
            }
            Category category = categoryOpt.get();

            System.out.print("Enter element name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            System.out.print("Enter element description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Description cannot be empty.");
                return;
            }

            Element element = new Element();
            element.setName(name);
            element.setDescription(description);
            element.setCategory(category);
            
            Element savedElement = elementService.save(element);
            
            category.getElements().add(savedElement);
            categoryService.save(category);

            System.out.println("Element added.");
            
        } catch (Exception e) {
            System.out.println("Error adding element: " + e.getMessage());
        }
    }

    @Transactional
    private void deleteElement() {
        try {
            System.out.println("\nCurrent elements:");
            var elements = elementService.findAll();
            if (elements.isEmpty()) {
                System.out.println("No elements available to delete.");
                return;
            }
            elements.forEach(element ->
                System.out.println(element.getId() + ": " + element.getName()));

            System.out.print("\nEnter element ID to delete: ");
            String elementIdStr = scanner.nextLine().trim();
            
            UUID elementId;
            try {
                elementId = UUID.fromString(elementIdStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format.");
                return;
            }

            Optional<Element> elementOpt = elementService.findById(elementId);
            if (elementOpt.isEmpty()) {
                System.out.println("Element not found.");
                return;
            }

            Element element = elementOpt.get();
            Category category = element.getCategory();
            if (category != null) {
                category.getElements().remove(element);
                categoryService.save(category);
            }
            
            elementService.deleteById(elementId);
            System.out.println("Element deleted.");
            
        } catch (Exception e) {
            System.out.println("Error deleting element: " + e.getMessage());
        }
    }

    private void exit() {
        System.out.println("\nShutting down.");
        running = false;
        context.close();
    }
}
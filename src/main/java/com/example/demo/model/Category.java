package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends BaseEntity {
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Element> elements = new HashSet<>();

    public void addElement(Element element) {
        elements.add(element);
        element.setCategory(this);
    }

    public void removeElement(Element element) {
        elements.remove(element);
        element.setCategory(null);
    }
}
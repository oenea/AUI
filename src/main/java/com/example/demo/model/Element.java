package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "elements")
@Getter
@Setter
public class Element extends BaseEntity {
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void setCategory(Category category) {
        if (this.category != null) {
            this.category.getElements().remove(this);
        }
        this.category = category;
        if (category != null && !category.getElements().contains(this)) {
            category.getElements().add(this);
        }
    }
}
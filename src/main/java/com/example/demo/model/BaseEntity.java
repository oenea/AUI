package com.example.demo.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    private UUID id = UUID.randomUUID();
}
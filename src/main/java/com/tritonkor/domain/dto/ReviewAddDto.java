package com.tritonkor.domain.dto;

import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.entity.Entity;
import com.tritonkor.persistence.entity.Grade;
import com.tritonkor.persistence.entity.Technique;
import java.time.LocalDateTime;

public class ReviewAddDto extends Entity {
    /** The client who owns the review. */
    private Client owner;

    /** The technique associated with the review. */
    private Technique technique;

    /** The text content of the review. */
    private String text;

    /** The grade assigned to the review. */
    private Grade grade;

    /** The timestamp when the review was created. */
    private LocalDateTime createdAt;

    public ReviewAddDto(int id, Client owner, Technique technique, String text, Grade grade,
            LocalDateTime createdAt) {
        super(id);
        this.owner = owner;
        this.technique = technique;
        this.text = text;
        this.grade = grade;
        this.createdAt = createdAt;
    }

    public Client getOwner() {
        return owner;
    }

    public Technique getTechnique() {
        return technique;
    }

    public String getText() {
        return text;
    }

    public Grade getGrade() {
        return grade;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

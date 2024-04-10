package com.tritonkor.persistence.entity;

import com.tritonkor.persistence.exception.RequiredFieldsException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a review entity in the system with a unique identifier, owner, technique, text, grade, and creation timestamp.
 */
public class Review extends Entity {

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

    public Review(ReviewBuilder reviewBuilder) {
        super(reviewBuilder.id);
        this.owner = reviewBuilder.owner;
        this.technique = reviewBuilder.technique;
        this.text = reviewBuilder.text;
        this.grade = reviewBuilder.grade;
        this.createdAt = reviewBuilder.createdAt;
    }

    /**
     * Gets the client who owns the review.
     *
     * @return The owner client.
     */
    public Client getOwner() {
        return owner;
    }

    /**
     * Gets the technique associated with the review.
     *
     * @return The associated technique.
     */
    public Technique getTechnique() {return technique;}

    /**
     * Gets the text content of the review.
     *
     * @return The review text.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the grade assigned to the review.
     *
     * @return The assigned grade.
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * Gets the timestamp when the review was created.
     *
     * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static ReviewBuilder builder() {
        return new ReviewBuilder();
    }

    public static class ReviewBuilder {

        private int id;
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


        public ReviewBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ReviewBuilder owner(Client owner) {
            this.owner = owner;
            return this;
        }

        public ReviewBuilder technique(Technique technique) {
            this.technique = technique;
            return this;
        }

        public ReviewBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ReviewBuilder grade(int grade) {
            this.grade = new Grade(grade);
            return this;
        }

        public ReviewBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Review build() {
            if (Stream.of(text).anyMatch(s -> Objects.isNull(s) || s.isBlank())) {
                throw new RequiredFieldsException();
            }
            return new Review(this);
        }
    }

    /**
     * Returns a string representation of the review.
     *
     * @return A string representation with details of the review.
     */
    @Override
    public String toString() {
        return "Review{" +
                "owner=" + owner.getUsername() +
                ", technique='" + technique.getModel() + '\'' +
                ", text='" + text + '\'' +
                ", grade=" + grade +
                ", createdAt=" + createdAt +
                '}';
    }
}

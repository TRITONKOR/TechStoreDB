package com.tritonkor.persistence.filter;

import java.time.LocalDateTime;

public record ReviewFilter (int limit, int offset, int ownerID, int techniqueID, String text, int grade, LocalDateTime createdAt) {

    public static class ReviewFilterBuilder {

        private int limit;
        private int offset;
        private int ownerID;
        private int techniqueID;
        private String text;
        private int grade;
        private LocalDateTime createdAt;

        public ReviewFilterBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public ReviewFilterBuilder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public ReviewFilterBuilder ownerID(int ownerID) {
            this.ownerID = ownerID;
            return this;
        }

        public ReviewFilterBuilder techniqueID(int techniqueID) {
            this.techniqueID = techniqueID;
            return this;
        }

        public ReviewFilterBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ReviewFilterBuilder grade(int grade) {
            this.grade = grade;
            return this;
        }

        public ReviewFilterBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReviewFilter build() {
            return new ReviewFilter(limit, offset, ownerID, techniqueID, text, grade, createdAt);
        }
    }
}

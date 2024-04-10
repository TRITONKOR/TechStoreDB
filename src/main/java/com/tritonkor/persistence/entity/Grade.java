package com.tritonkor.persistence.entity;

/**
 * The {@code Grade} class represents a grade with its associated properties.
 */

public class Grade {

    /**
     * The maximum possible grade.
     */
    private final int MAX_GRADE = 10;

    /**
     * The value of grade.
     */
    private int grade;

    /**
     * Constructs a {@code Grade} instance with the specified grade value.
     *
     * @param grade The grade value.
     */
    public Grade(int grade) {

        if (grade > 10) {
            grade = 10;
        } else if (grade < 1) {
            grade = 1;
        }
        this.grade = grade;
    }

    /**
     * Gets the grade value.
     *
     * @return The grade value.
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Gets the max grade value.
     *
     * @return The grade value.
     */
    public int getMaxGrade() {
        return MAX_GRADE;
    }

    /**
     * Sets the grade value.
     *
     * @param grade The new grade value.
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Returns a string representation of the grade.
     *
     * @return A string representation of the grade.
     */
    @Override
    public String toString() {
        return "Grade:" + grade + "/" + MAX_GRADE;
    }
}

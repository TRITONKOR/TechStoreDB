package com.tritonkor.persistence.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.util.DbInitialization;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReviewDaoTest {

    ReviewDao reviewDao;
    TechniqueDao techniqueDao;
    ClientDao clientDao;

    @BeforeEach
    void setup() {
        DbInitialization.apply();

        reviewDao = ReviewDao.getInstance();
        techniqueDao = TechniqueDao.getInstance();
        clientDao = ClientDao.getInstance();
    }

    @Test
    void testGetReviews() {
        List<Review> reviews = reviewDao.findAll();
        assertInstanceOf(List.class, reviews,
                "Помилка: метод findAll повинен повертати List<>");
        assertFalse(reviews.isEmpty(),
                "Помилка: повернений список не може бути пустим");
    }

    @Test
    void testAddReview() {
        Review reviewToAdd = Review.builder().owner(clientDao.findOneById(1).orElseThrow())
                .technique(techniqueDao.findOneById(1).orElseThrow()).grade(5).text("dadada")
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();

        reviewDao.save(reviewToAdd);

        List<Review> reviews = reviewDao.findAll();

        System.out.println(reviewDao.findAll());

        assertTrue(reviews.contains(reviewToAdd), "Помилка: список не містить доданого відгуку");
    }

    @Test
    void testUpdateReview() {
        Review review = reviewDao.findOneById(1).orElseThrow();

        review.setGrade(2);

        reviewDao.update(review);

        List<Review> reviews = reviewDao.findAll();

        assertTrue(reviews.contains(review), "Помилка: відгук не обновився");
    }
}

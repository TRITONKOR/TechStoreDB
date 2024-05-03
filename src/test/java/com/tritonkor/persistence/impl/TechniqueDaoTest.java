package com.tritonkor.persistence.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.util.DbInitialization;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TechniqueDaoTest {

    TechniqueDao techniqueDao;
    ClientDao clientDao;

    @BeforeEach
    void setup() {
        DbInitialization.apply();

        techniqueDao = TechniqueDao.getInstance();
        clientDao = ClientDao.getInstance();
    }

    @Test
    void testGetTechniques() {
        List<Technique> techniques = techniqueDao.findAll();
        assertInstanceOf(List.class, techniques,
                "Помилка: метод findAll повинен повертати List<>");
        assertFalse(techniques.isEmpty(),
                "Помилка: повернений список не може бути пустим");
    }

    @Test
    void testAddClient() {
        Technique techniqueToAdd = Technique.builder().price(200.00).company("Asus").model("da")
                .build();

        techniqueDao.save(techniqueToAdd);

        List<Technique> techniques = techniqueDao.findAll();

        assertTrue(techniques.contains(techniqueToAdd),
                "Помилка: список не містить доданого пристрою");
    }

    @Test
    void testUpdateTechnique() {
        Technique technique = techniqueDao.findOneById(1).orElseThrow();

        technique.setPrice(20.00);

        techniqueDao.update(technique);

        List<Technique> techniques = techniqueDao.findAll();
        assertTrue(techniques.contains(technique), "Помилка: пристрій не обновився");
    }
}

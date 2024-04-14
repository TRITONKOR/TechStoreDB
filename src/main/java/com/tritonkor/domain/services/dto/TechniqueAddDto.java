package com.tritonkor.domain.services.dto;

import com.tritonkor.persistence.entity.Entity;

public class TechniqueAddDto extends Entity {

    /** The price of the technique. */
    private double price;

    /** The company producing the technique. */
    private String company;

    /** The model name of the technique. */
    private String model;

    public TechniqueAddDto(int id, double price, String company, String model) {
        super(id);
        this.price = price;
        this.company = company;
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public String getCompany() {
        return company;
    }

    public String getModel() {
        return model;
    }
}

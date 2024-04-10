package com.tritonkor.persistence.entity;

import com.tritonkor.persistence.exception.RequiredFieldsException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a technique entity in the system with a unique identifier, price, company, and model.
 */
public class Technique extends Entity {

    /** The price of the technique. */
    private double price;

    /** The company producing the technique. */
    private final String company;

    /** The model name of the technique. */
    private final String model;

    public Technique(TechniqueBuilder techniqueBuilder) {
        super(techniqueBuilder.id);
        this.price = techniqueBuilder.price;
        this.company = techniqueBuilder.company;
        this.model = techniqueBuilder.model;
    }

    /**
     * Gets the price of the technique.
     *
     * @return The technique's price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the technique.
     *
     * @param price The new price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the company producing the technique.
     *
     * @return The producing company.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Gets the model name of the technique.
     *
     * @return The model name.
     */
    public String getModel() {
        return model;
    }

    public static TechniqueBuilder builder() {
        return new TechniqueBuilder();
    }

    public static class TechniqueBuilder {

        private int id;
        /** The price of the technique. */
        private double price;

        /** The company producing the technique. */
        private String company;

        /** The model name of the technique. */
        private String model;


        public TechniqueBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TechniqueBuilder model(String model) {
            this.model = model;
            return this;
        }

        public TechniqueBuilder company(String company) {
            this.company = company;
            return this;
        }

        public TechniqueBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public Technique build() {
            if (Stream.of(model, company).anyMatch(s -> Objects.isNull(s) || s.isBlank())) {
                throw new RequiredFieldsException();
            }
            return new Technique(this);
        }
    }

    /**
     * Returns a string representation of the technique.
     *
     * @return A string representation in the format "Technique{price=..., company='...', model='...'}".
     */
    @Override
    public String toString() {
        return "Technique{" +
                "price=" + price +
                ", company='" + company + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}

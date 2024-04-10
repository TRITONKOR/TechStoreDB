package com.tritonkor.persistence.filter;

public record TechniqueFilter (int limit, int offset, double price, String company, String model){

    public static TechniqueFilterBuilder builder() {
        return new TechniqueFilterBuilder();
    }

    public static class TechniqueFilterBuilder {

        private int limit;
        private int offset;
        private double price;
        private String company;
        private String model;

        public TechniqueFilterBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public TechniqueFilterBuilder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public TechniqueFilterBuilder price(double price) {
            this.price = price;
            return this;
        }

        public TechniqueFilterBuilder company(String company) {
            this.company = company;
            return this;
        }

        public TechniqueFilterBuilder model(String model) {
            this.model = model;
            return this;
        }

        public TechniqueFilter build() {
            return new TechniqueFilter(limit, offset, price, company, model);
        }
    }
}

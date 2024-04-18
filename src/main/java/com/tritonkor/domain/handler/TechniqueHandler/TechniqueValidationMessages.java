package com.tritonkor.domain.handler.TechniqueHandler;

public enum TechniqueValidationMessages {

    EMPTY("The %s field cant by empty."),
    ONLY_LATIN("The %s field can contain only Latin letters."),
    MAX_LENGTH("The %s field cannot be more than 50 symbols."),
    MIN_PRICE("The price field cannot be less than 0.00$.");

    private String template;

    TechniqueValidationMessages(String template) {
        this.template = template;
    }

    /**
     * Gets the template associated with the error type.
     *
     * @return The template string.
     */
    public String getTemplate() {
        return template;
    }
}

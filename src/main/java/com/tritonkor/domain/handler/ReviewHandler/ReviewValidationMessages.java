package com.tritonkor.domain.handler.ReviewHandler;

public enum ReviewValidationMessages {

    EMPTY("The %s field cant by empty"),
    ONLY_LATIN("The %s field can contain only Latin letters"),
    MIN_LENGTH("The %s field cannot be less than 6 symbols."),
    TEXT_MAX_LENGTH("The text field cannot be more than 256 symbols."),
    DATE("The %s field cannot contain a date from the future");

    private String template;

    ReviewValidationMessages(String template) {
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

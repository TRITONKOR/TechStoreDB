package com.tritonkor.domain.handler.ClientHandler;

public enum ClientValidationMessages {
    EMPTY("The %s field cant by empty"),
    ONLY_LATIN("The %s field can contain only Latin letters"),
    MIN_LENGTH("The %s field cannot be less than 6 symbols."),
    USERNAME_MAX_LENGTH("The username field cannot be more then 16 symbols."),
    PASSWORD_MAX_LENGTH("The password field cannot be more than 32 symbols.");

    private String template;

    ClientValidationMessages(String template) {
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

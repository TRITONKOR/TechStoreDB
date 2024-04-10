package com.tritonkor.persistence.filter;

public record ClientFilter(int limit, int offset, String username) {

    public static UserFilterBuilder builder() {
        return new UserFilterBuilder();
    }

    public static class UserFilterBuilder {

        private int limit;
        private int offset;
        private String username;

        public UserFilterBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserFilterBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public UserFilterBuilder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public ClientFilter build() {
            return new ClientFilter(limit, offset, username);
        }
    }
}

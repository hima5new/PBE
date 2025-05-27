package com.epam.restaurant.pojos;

public class AuthenticationPojo {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final int expectedStatusCode;

    private AuthenticationPojo(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.expectedStatusCode = builder.expectedStatusCode;
    }

    public String getFirstName() {
        return firstName; }
    public String getLastName() {
        return lastName; }
    public String getEmail() {
        return email; }
    public String getPassword() {
        return password; }

    public int getExpectedStatusCode() { return expectedStatusCode; }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private int expectedStatusCode;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setExpectedStatusCode(int expectedStatusCode) {
            this.expectedStatusCode = expectedStatusCode;
            return this;
        }


        public AuthenticationPojo build() {
            return new AuthenticationPojo(this);
        }
    }
}

package com.epam.restaurant.pojos;

public class SigninPojo {
    private String email;
    private String password;

    private SigninPojo(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }

    public static class Builder {
        private String email;
        private String password;

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public SigninPojo build() {
            return new SigninPojo(this);
        }
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}



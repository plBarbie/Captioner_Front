package com.example.captioner.network;

public class UserResponse {
    private String token;
    public class userResponse {
        private String email;
        private String name;
        private String Token;


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // 如果还有其他字段，也应该为它们提供getters和setters
}
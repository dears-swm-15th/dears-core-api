package com.example.demo.error.custom;

public class PortfolioNotFoundException extends RuntimeException {

    public PortfolioNotFoundException(String message) {
        super(message);
    }
}

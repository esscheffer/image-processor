package com.bix.image_processor.image_processing;

public class QuotaExceededException extends RuntimeException {
    public QuotaExceededException(String message) {
        super(message);
    }
}

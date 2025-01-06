package com.worktree.hrms.exceptions;

public class PaymentRequiredException extends RuntimeException {

    public PaymentRequiredException(String message) {
        super(message);
    }

}

package com.worktree.hrms.exceptions;

public class WorktreeException extends RuntimeException {

    public WorktreeException(String message) {
        super(message);
    }

    public WorktreeException(String message, Throwable cause) {
        super(message, cause);
    }
}

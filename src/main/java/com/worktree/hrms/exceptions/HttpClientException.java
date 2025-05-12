package com.worktree.hrms.exceptions;

public class HttpClientException extends RuntimeException {

    private final int statusCode;
    private final transient Object body;

    public HttpClientException(String message, int statusCode, Object body) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getBody() {
        return body;
    }
}

package com.slash.project.myfoli.global.auth.jwt.exception;

public enum TokenValidationResult {
    VALID,
    EXPIRED,
    INVALID_SIGNATURE,
    MALFORMED
}

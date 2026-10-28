package io.hiusnef.sampleservice.shared.web.error;

import io.hiusnef.sampleservice.shared.kernel.error.DomainException;
import io.hiusnef.sampleservice.shared.kernel.error.ErrorCode;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Maps domain exceptions of every bounded context to RFC 9457 problem responses; framework errors (validation, 404 on
 * unknown routes...) are handled by the superclass.
 */
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TYPE_BASE = "https://hiusnef.io/problems/";

    @ExceptionHandler(DomainException.class)
    ProblemDetail handleDomainException(DomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status(ex.code()), ex.getMessage());
        problem.setType(URI.create(TYPE_BASE + ex.code().slug()));
        return problem;
    }

    // Exhaustive switch: adding an ErrorCode without an HTTP mapping does not compile.
    private static HttpStatus status(ErrorCode code) {
        return switch (code) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
        };
    }
}

package io.hiusnef.sampleservice.shared.kernel.error;

public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}

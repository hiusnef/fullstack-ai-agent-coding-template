package io.hiusnef.sampleservice.shared.kernel.error;

public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}

package local.devicemanagement.application.exception;

import static local.devicemanagement.application.exception.LongHelper.safe;


public class ConcurrentUpdateException extends RuntimeException {

    public ConcurrentUpdateException(String id) {
        super("Device with id:" + id + " was modified concurrently");
    }

    public ConcurrentUpdateException(Long id) {
        this(safe(id));
    }

}

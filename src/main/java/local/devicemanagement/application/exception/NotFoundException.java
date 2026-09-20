package local.devicemanagement.application.exception;

import static local.devicemanagement.application.exception.LongHelper.safe;


public class NotFoundException extends RuntimeException{

    public NotFoundException(String id) {
        super("Device with id:" + id + " not found");
    }

    public NotFoundException(Long id) {
        this(safe(id));
    }

}

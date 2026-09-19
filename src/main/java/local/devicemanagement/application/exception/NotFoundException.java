package local.devicemanagement.application.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String id) {
        super("Device with id:" + id + " not found");
    }

    public NotFoundException(Integer id) {
        this(id == null ? "NULL" : id.toString());
    }

}

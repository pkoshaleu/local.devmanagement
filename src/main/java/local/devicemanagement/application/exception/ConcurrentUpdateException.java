package local.devicemanagement.application.exception;

public class ConcurrentUpdateException extends RuntimeException {

    public ConcurrentUpdateException(String id) {
        super("Device with id:" + id + " was modified concurrently");
    }

    public ConcurrentUpdateException(Integer id) {
        this(id == null ? "NULL" : id.toString());
    }

}

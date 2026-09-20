package local.devicemanagement.application.exception;

public final class LongHelper {

    private LongHelper() {}

    /*package*/ static String safe(Long value) {
        return value == null ? "NULL" : value.toString();
    }

}

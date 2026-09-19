package local.devicemanagement.application.exception;

import local.devicemanagement.domain.model.State;


public class IllicitStateException extends RuntimeException {

    public IllicitStateException(String id, String state) {
        super("Device with id:" + id + " in state:" + state + " now allowed to modified");
    }

    public IllicitStateException(Integer id, State state) {
        this(id == null ? "NULL" : id.toString(), state.name());
    }

}

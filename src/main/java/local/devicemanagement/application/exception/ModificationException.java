package local.devicemanagement.application.exception;

import local.devicemanagement.domain.model.State;


public class ModificationException extends RuntimeException {

    public ModificationException(String id, String state) {
        super("Device with id:" + id + " in state:" + state + " not allowed to be modified");
    }

    public ModificationException(Integer id, State state) {
        this(id == null ? "NULL" : id.toString(), state.name());
    }

}

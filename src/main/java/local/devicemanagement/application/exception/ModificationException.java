package local.devicemanagement.application.exception;

import local.devicemanagement.domain.model.State;

import static local.devicemanagement.application.exception.LongHelper.safe;


public class ModificationException extends RuntimeException {

    public ModificationException(String id, String state) {
        super("Device with id:" + id + " in state:" + state + " not allowed to be modified");
    }

    public ModificationException(Long id, State state) {
        this(safe(id), state.name());
    }

}

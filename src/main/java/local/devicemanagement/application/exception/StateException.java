package local.devicemanagement.application.exception;

import local.devicemanagement.domain.model.State;


public class StateException extends RuntimeException {

    public StateException(String id, String current, String next) {
        super("Device with id:" + id + " state transition from:" + current + " to: " + next + " not allowed");
    }

    public StateException(Integer id, State current, State next) {
        this(id == null ? "NULL" : id.toString(), current.name(), next.name());
    }

}

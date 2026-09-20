package local.devicemanagement.application.exception;

import local.devicemanagement.domain.model.State;

import static local.devicemanagement.application.exception.LongHelper.safe;


public class StateException extends RuntimeException {

    public StateException(String id, String current, String next) {
        super("Device with id:" + id + " state transition from:" + current + " to: " + next + " not allowed");
    }

    public StateException(Long id, State current, State next) {
        this(safe(id), current.name(), next.name());
    }

}

package local.devicemanagement.domain.model;

import java.util.Map;
import java.util.Set;


public enum State {

    AVAILABLE,
    IN_USE,
    INACTIVE;

    private static final Map<State, Set<State>> TRANSITIONS = Map.of(
            AVAILABLE, Set.of(IN_USE, INACTIVE),
            IN_USE, Set.of(AVAILABLE),
            INACTIVE, Set.of(AVAILABLE)
    );

    public boolean isAllowed(State next) {
        return this == next || TRANSITIONS.get(this).contains(next);
    }

}

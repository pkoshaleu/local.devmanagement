package local.devicemanagement.presentation.response;

import local.devicemanagement.domain.model.State;

import java.time.Instant;


public record DeviceResponse (
    Long id,
    String name,
    String brand,
    State state,
    Instant createdAt,
    Instant updatedAt
) {
    //~
}

package local.devicemanagement.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Device {

    Long id;
    String name;
    String brand;
    State state;
    Instant createdAt;
    Instant updatedAt;

}

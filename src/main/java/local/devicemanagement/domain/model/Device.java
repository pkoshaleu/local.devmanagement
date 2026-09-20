package local.devicemanagement.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;


@Value
@Builder(toBuilder = true)
public class Device {

    Integer id;
    String name;
    String brand;
    State state;
    Instant createdAt;
    Instant updatedAt;
    Integer version;

}

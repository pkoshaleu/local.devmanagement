package local.devicemanagement.presentation.request;

import jakarta.validation.constraints.Size;

import local.devicemanagement.domain.model.State;
import local.devicemanagement.presentation.validation.NullOrNotBlank;


public record DeviceFilterRequest(
        @NullOrNotBlank @Size(max = 255) String brand,
        @NullOrNotBlank @Size(max = 255) String name,
        State state
) {
}

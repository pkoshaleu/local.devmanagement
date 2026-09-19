package local.devicemanagement.presentation.request;

import jakarta.validation.constraints.Size;


public record PatchDeviceRequest(
        @Size(min = 1, max = 256) String name,
        @Size(min = 1, max = 256) String brand
) {
}

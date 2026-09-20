package local.devicemanagement.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CreateDeviceRequest(
        @NotBlank @Size(min = 1, max = 255) String name,
        @NotBlank @Size(min = 1, max = 255) String brand
) {
}

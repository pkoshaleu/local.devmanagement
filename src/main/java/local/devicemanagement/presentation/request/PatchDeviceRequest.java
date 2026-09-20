package local.devicemanagement.presentation.request;

import jakarta.validation.constraints.Size;

import local.devicemanagement.presentation.validation.NullOrNotBlank;


public record PatchDeviceRequest(
        @NullOrNotBlank @Size(min = 1, max = 255) String name,
        @NullOrNotBlank @Size(min = 1, max = 255) String brand
) {
}

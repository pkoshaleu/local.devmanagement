package local.devicemanagement.presentation.request;

import jakarta.validation.constraints.Size;

import local.devicemanagement.presentation.validation.NullOrNotBlank;


public record PatchDeviceRequest(
        @NullOrNotBlank @Size(max = 256) String name,
        @NullOrNotBlank @Size(max = 256) String brand
) {
}

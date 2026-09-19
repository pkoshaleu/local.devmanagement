package local.devicemanagement.presentation.request;

import jakarta.validation.constraints.NotNull;

import local.devicemanagement.domain.model.State;


public record ChangeStateRequest(
        @NotNull State state
) {
}

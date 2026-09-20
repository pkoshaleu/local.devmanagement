package local.devicemanagement.domain.model;


public record DeviceFilter(
        String brand,
        String name,
        State state
) {
    //~
}

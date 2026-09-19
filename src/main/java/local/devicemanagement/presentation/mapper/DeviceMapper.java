package local.devicemanagement.presentation.mapper;

import org.mapstruct.Mapper;

import local.devicemanagement.domain.model.Device;
import local.devicemanagement.presentation.response.DeviceResponse;


@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceResponse toResponse(Device device);

}

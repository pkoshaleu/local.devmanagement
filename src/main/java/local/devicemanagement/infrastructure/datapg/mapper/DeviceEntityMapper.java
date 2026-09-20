package local.devicemanagement.infrastructure.datapg.mapper;

import org.mapstruct.Mapper;

import local.devicemanagement.domain.model.Device;
import local.devicemanagement.infrastructure.datapg.entity.DeviceEntity;


@Mapper(componentModel = "spring")
public interface DeviceEntityMapper {

    DeviceEntity toEntity(Device device);

    Device toDomain(DeviceEntity entity);

}

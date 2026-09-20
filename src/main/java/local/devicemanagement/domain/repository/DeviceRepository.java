package local.devicemanagement.domain.repository;

import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.DeviceFilter;

import java.util.List;
import java.util.Optional;


public interface DeviceRepository {

    Optional<Device> findById(Integer id);

    List<Device> findAll(DeviceFilter filter);

    Device save(Device device);

    void delete(Device device);

}

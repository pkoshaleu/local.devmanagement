package local.devicemanagement.domain.repository;

import local.devicemanagement.domain.model.Device;

import java.util.List;
import java.util.Optional;


public interface DeviceRepository {

    Optional<Device> findById(Integer id);

    List<Device> findAll();

    Device save(Device device);

    void delete(Device device);

}

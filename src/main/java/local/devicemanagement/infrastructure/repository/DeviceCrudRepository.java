package local.devicemanagement.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;

import local.devicemanagement.infrastructure.datapg.entity.DeviceEntity;


public interface DeviceCrudRepository extends CrudRepository<DeviceEntity, Integer> {

}

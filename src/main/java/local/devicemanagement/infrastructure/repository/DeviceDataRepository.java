package local.devicemanagement.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import local.devicemanagement.application.exception.ConcurrentUpdateException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.repository.DeviceRepository;
import local.devicemanagement.infrastructure.datapg.entity.DeviceEntity;
import local.devicemanagement.infrastructure.datapg.mapper.DeviceEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Repository
@RequiredArgsConstructor
public class DeviceDataRepository implements DeviceRepository {

    private final DeviceCrudRepository repository;
    private final DeviceEntityMapper mapper;

    @Override
    public Optional<Device> findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Device> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Device save(Device device) {
        try {
            DeviceEntity saved = repository.save(mapper.toEntity(device));
            return mapper.toDomain(saved);
        } catch (OptimisticLockingFailureException ex) {
            throw new ConcurrentUpdateException(device.getId());
        }
    }

    @Override
    public void delete(Device device) {
        repository.delete(mapper.toEntity(device));
    }

}

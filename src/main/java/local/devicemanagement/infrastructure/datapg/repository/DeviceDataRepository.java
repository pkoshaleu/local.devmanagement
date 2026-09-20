package local.devicemanagement.infrastructure.datapg.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import local.devicemanagement.application.exception.ConcurrentUpdateException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.DeviceFilter;
import local.devicemanagement.domain.repository.DeviceRepository;
import local.devicemanagement.infrastructure.datapg.entity.DeviceEntity;
import local.devicemanagement.infrastructure.datapg.mapper.DeviceEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class DeviceDataRepository implements DeviceRepository {

    private final DeviceCrudRepository repository;
    private final JdbcAggregateTemplate template;
    private final DeviceEntityMapper mapper;

    @Override
    public Optional<Device> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Device> findAll(DeviceFilter filter) {
        List<Criteria> criteria = new ArrayList<>();

        if (filter.name() != null) {
            criteria.add(Criteria.where("name").like(filter.name() + "%").ignoreCase(true));
        }

        if (filter.brand() != null) {
            criteria.add(Criteria.where("brand").is(filter.brand()));
        }

        if (filter.state() != null) {
            criteria.add(Criteria.where("state").is(filter.state()));
        }

        Query query = criteria.isEmpty() ? Query.empty() : Query.query(Criteria.from(criteria));

        return template.findAll(query, DeviceEntity.class).stream()
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

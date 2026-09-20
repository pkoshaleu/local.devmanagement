package local.devicemanagement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import local.devicemanagement.application.exception.ModificationException;
import local.devicemanagement.application.exception.NotFoundException;
import local.devicemanagement.application.exception.StateException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.DeviceFilter;
import local.devicemanagement.domain.model.State;
import local.devicemanagement.domain.repository.DeviceRepository;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;
    private final TimeService timeService;

    @Transactional
    public Device create(String name, String brand) {
        Instant now = timeService.now();
        Device device = Device.builder()
                .name(name)
                .brand(brand)
                .state(State.AVAILABLE)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return repository.save(device);
    }

    @Transactional(readOnly = true)
    public Device getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Device> getAll(DeviceFilter filter) {
        return repository.findAll(filter);
    }

    @Transactional
    public Device updateDevice(Long id, String name, String brand) {
        Device device = guardState(getById(id));

        Device.DeviceBuilder builder = device.toBuilder();
        if (name != null) {
            builder = builder.name(name);
        }
        if (brand != null) {
            builder = builder.brand(brand);
        }

        Device updated = builder.updatedAt(timeService.now()).build();
        return repository.save(updated);
    }

    @Transactional
    public Device updateState(Long id, State next) {
        Device device = getById(id);

        State current = device.getState();
        if (!current.isAllowed(next)) {
            throw new StateException(id, current, next);
        }

        var updated = device.toBuilder().state(next).updatedAt(timeService.now()).build();
        return repository.save(updated);
    }

    @Transactional
    public void delete(Long id) {
        Device device = guardState(getById(id));
        repository.delete(device);
    }

    private static Device guardState(Device device) {
        if (device.getState() == State.IN_USE) {
            throw new ModificationException(device.getId(), device.getState());
        }
        return device;
    }

}

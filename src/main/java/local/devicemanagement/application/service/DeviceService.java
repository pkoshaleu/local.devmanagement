package local.devicemanagement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import local.devicemanagement.application.exception.IllicitStateException;
import local.devicemanagement.application.exception.NotFoundException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.State;
import local.devicemanagement.domain.repository.DeviceRepository;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;
    private final TimeService timeService;

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

    public Device getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public List<Device> getAll() {
        //TODO: filtration!
        return repository.findAll();
    }

    public Device update(Integer id, String name, String brand) {
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

    public void delete(Integer id) {
        Device device = guardState(getById(id));
        repository.delete(device);
    }

    private static Device guardState(Device device) {
        if (device.getState() == State.IN_USE) {
            throw new IllicitStateException(device.getId(), device.getState());
        }
        return device;
    }

}

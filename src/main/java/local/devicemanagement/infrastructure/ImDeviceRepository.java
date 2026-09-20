package local.devicemanagement.infrastructure;

import org.springframework.stereotype.Component;

import local.devicemanagement.application.exception.ConcurrentUpdateException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.repository.DeviceRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;


//@Component
public class ImDeviceRepository implements DeviceRepository {

    private static Integer lastId = 1;
    private final Map<Integer, Device> devices = new HashMap<>();

    @Override
    public Optional<Device> findById(Integer id) {
        return Optional.ofNullable(devices.get(id));
    }

    @Override
    public List<Device> findAll() {
        return new ArrayList<>(devices.values());
    }

    @Override
    public Device save(Device device) {
        if (device.getId() != null) {
            Device current = devices.get(device.getId());
            if (current == null || !current.getVersion().equals(device.getVersion())) {
                throw new ConcurrentUpdateException(device.getId());
            }
        }

        if (device.getId() == null) {
            return put(() -> device.toBuilder().id(nextId()).version(0).build());
        } else {
            return put(() -> device.toBuilder().version(nextVersion(device.getVersion())).build());
        }
    }

    private Device put(Supplier<Device> builder) {
        Device device = builder.get();
        devices.put(device.getId(), device);
        return device;
    }

    @Override
    public void delete(Device device) {
        devices.remove(device.getId());
    }

    //~

    private static int nextId() {
        return lastId++;
    }

    private static int nextVersion(Integer current) {
        return current == null ? 0 : current + 1;
    }

}

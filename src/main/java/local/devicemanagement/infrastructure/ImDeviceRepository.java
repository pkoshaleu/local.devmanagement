package local.devicemanagement.infrastructure;

import org.springframework.stereotype.Component;

import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.repository.DeviceRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class ImDeviceRepository implements DeviceRepository {

    private Integer nextId = 1;
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
        Device toStore = device.getId() == null
                ? device.toBuilder().id(nextId++).build()
                : device;
        devices.put(toStore.getId(), toStore);
        return toStore;
    }

    @Override
    public void delete(Device device) {
        devices.remove(device.getId());
    }

}

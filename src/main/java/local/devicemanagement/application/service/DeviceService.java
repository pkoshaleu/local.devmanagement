package local.devicemanagement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.repository.DeviceRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;

    public Device create(Device device) {
        return null;
    }

    public Device getById(Integer id) {
        return null;
    }

    public List<Device> getAll() {
        return List.of();
    }

}

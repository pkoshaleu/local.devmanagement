package local.devicemanagement.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import local.devicemanagement.application.service.DeviceService;
import local.devicemanagement.presentation.mapper.DeviceMapper;
import local.devicemanagement.presentation.request.ChangeStateRequest;
import local.devicemanagement.presentation.request.CreateDeviceRequest;
import local.devicemanagement.presentation.request.DeviceFilterRequest;
import local.devicemanagement.presentation.request.PatchDeviceRequest;
import local.devicemanagement.presentation.response.DeviceResponse;

import java.util.List;


@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;
    private final DeviceMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse create(@Valid @RequestBody CreateDeviceRequest request) {
        return mapper.toResponse(service.create(request.name(), request.brand()));
    }

    @GetMapping
    public List<DeviceResponse> getAll(@Valid DeviceFilterRequest request) {
        var filter = mapper.toFilter(request);
        return service.getAll(filter).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable Integer id) {
        var device = service.getById(id);
        return mapper.toResponse(device);
    }

    @PatchMapping("/{id}")
    public DeviceResponse update(@PathVariable Integer id, @Valid @RequestBody PatchDeviceRequest request) {
        var device = service.updateDevice(id, request.name(), request.brand());
        return mapper.toResponse(device);
    }

    @PutMapping("/{id}/state")
    public DeviceResponse updateState(@PathVariable Integer id, @Valid @RequestBody ChangeStateRequest request) {
        var device = service.updateState(id, request.state());
        return mapper.toResponse(device);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

}

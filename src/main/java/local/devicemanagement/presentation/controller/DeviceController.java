package local.devicemanagement.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import local.devicemanagement.application.service.DeviceService;
import local.devicemanagement.presentation.mapper.DeviceMapper;
import local.devicemanagement.presentation.request.CreateDeviceRequest;
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
        return null;
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable Integer id) {
        var device = service.getById(id);
        return mapper.toResponse(device);
    }

    @GetMapping
    public List<DeviceResponse> getAll() {
        return service.getAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

}

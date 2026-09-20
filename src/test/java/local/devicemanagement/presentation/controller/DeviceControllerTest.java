package local.devicemanagement.presentation.controller;

import local.devicemanagement.application.service.DeviceService;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.State;
import local.devicemanagement.presentation.mapper.DeviceMapper;
import local.devicemanagement.presentation.response.DeviceResponse;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    private static final Instant NOW_1 = Instant.parse("2026-09-20T08:00:00Z");
    private static final String NAME_1 = "Name 1";
    private static final String BRAND_1 = "Brand 1";
    private static final String BRAND_2 = "Brand 2";
    private static final Long ID_1 = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService service;

    @MockitoBean
    private DeviceMapper mapper;

    @Nested
    class Create {

        @Test
        void happyPath() throws Exception {
            Device device = Device.builder().id(ID_1).build();
            DeviceResponse response = new DeviceResponse(ID_1, NAME_1, BRAND_1, State.AVAILABLE, NOW_1, NOW_1);
            when(service.create(NAME_1, BRAND_1)).thenReturn(device);
            when(mapper.toResponse(device)).thenReturn(response);

            mockMvc.perform(
                    post("/api/devices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"name":"Name 1","brand":"Brand 1"}
                                    """)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(ID_1))
                    .andExpect(jsonPath("$.name").value(NAME_1))
                    .andExpect(jsonPath("$.brand").value(BRAND_1))
                    .andExpect(jsonPath("$.state").value(State.AVAILABLE.name()));

            verify(service).create(NAME_1, BRAND_1);
        }
    }

    @Nested
    class Update {

        @Test
        void happyPath() throws Exception {
            Device device = Device.builder().id(ID_1).build();
            DeviceResponse response = new DeviceResponse(ID_1, NAME_1, BRAND_2, State.AVAILABLE, NOW_1, NOW_1);
            when(service.updateDevice(ID_1, null, BRAND_2)).thenReturn(device);
            when(mapper.toResponse(device)).thenReturn(response);

            mockMvc.perform(
                    patch("/api/devices/{id}", ID_1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"brand":"Brand 2"}
                                    """)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ID_1))
                    .andExpect(jsonPath("$.brand").value(BRAND_2));

            verify(service).updateDevice(ID_1, null, BRAND_2);
        }
    }

    @Nested
    class UpdateState {

        @Test
        void happyPath() throws Exception {
            Device device = Device.builder().id(ID_1).build();
            DeviceResponse response = new DeviceResponse(ID_1, NAME_1, BRAND_1, State.IN_USE, NOW_1, NOW_1);
            when(service.updateState(ID_1, State.IN_USE)).thenReturn(device);
            when(mapper.toResponse(device)).thenReturn(response);

            mockMvc.perform(
                    put("/api/devices/{id}/state", ID_1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"state":"IN_USE"}
                                    """)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ID_1))
                    .andExpect(jsonPath("$.state").value(State.IN_USE.name()));

            verify(service).updateState(ID_1, State.IN_USE);
        }
    }

}

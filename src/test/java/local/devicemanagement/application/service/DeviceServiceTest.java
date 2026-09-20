package local.devicemanagement.application.service;

import local.devicemanagement.application.exception.NotFoundException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.State;
import local.devicemanagement.domain.repository.DeviceRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    private static final Instant NOW_1 = Instant.parse("2026-09-20T08:00:00Z");
    private static final String NAME_1 = "Name 1";
    private static final String BRAND_1 = "Brand 1";
    private static final Integer ID_1 = 1;


    @Mock
    private DeviceRepository repository;

    @Mock
    private TimeService timeService;

    @InjectMocks
    private DeviceService service;

    @Nested
    class Create {

        @Test
        void happyPath() {
            when(timeService.now()).thenReturn(NOW_1);

            Device persisted = Device.builder().id(1).build();
            when(repository.save(
                    org.mockito.ArgumentMatchers.any(Device.class))
            ).thenReturn(persisted);

            Device result = service.create(NAME_1, BRAND_1);

            assertThat(result).isSameAs(persisted);

            ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);
            verify(repository).save(captor.capture());
            Device saved = captor.getValue();
            assertAll(
                    () -> assertThat(saved.getName()).isEqualTo(NAME_1),
                    () -> assertThat(saved.getBrand()).isEqualTo(BRAND_1),
                    () -> assertThat(saved.getState()).isEqualTo(State.AVAILABLE),
                    () -> assertThat(saved.getCreatedAt()).isEqualTo(NOW_1),
                    () -> assertThat(saved.getUpdatedAt()).isEqualTo(NOW_1)
            );
        }
    }

    @Nested
    class GetById {

        @Test
        void found() {
            Device device = Device.builder().id(ID_1).name(NAME_1).build();
            when(repository.findById(ID_1)).thenReturn(Optional.of(device));

            Device result = service.getById(ID_1);

            assertThat(result).isSameAs(device);
        }

        @Test
        void notFound() {
            when(repository.findById(ID_1)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getById(ID_1))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ID_1.toString());
        }
    }

}

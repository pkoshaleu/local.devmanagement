package local.devicemanagement.application.service;

import local.devicemanagement.application.exception.ModificationException;
import local.devicemanagement.application.exception.NotFoundException;
import local.devicemanagement.application.exception.StateException;
import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.State;
import local.devicemanagement.domain.repository.DeviceRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    private static final Instant NOW_1 = Instant.parse("2026-09-20T08:00:00Z");
    private static final Instant BEF_1 = Instant.parse("2026-09-19T08:00:00Z");
    private static final String NAME_1 = "Name 1";
    private static final String NAME_2 = "Name 2";
    private static final String BRAND_1 = "Brand 1";
    private static final String BRAND_2 = "Brand 2";
    private static final Long ID_1 = 1L;


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

            Device persisted = Device.builder().id(1L).build();
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

    @Nested
    class UpdateState {

        @ParameterizedTest
        @CsvSource({
                "AVAILABLE, IN_USE",
                "AVAILABLE, INACTIVE",
                "IN_USE, AVAILABLE",
                "INACTIVE, AVAILABLE"
        })
        void allowed(State current, State next) {
            Device device = Device.builder().id(ID_1).state(current).updatedAt(BEF_1).build();
            when(repository.findById(ID_1)).thenReturn(Optional.of(device));
            when(timeService.now()).thenReturn(NOW_1);
            when(repository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Device result = service.updateState(ID_1, next);

            ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);
            verify(repository).save(captor.capture());
            Device saved = captor.getValue();
            assertAll(
                    () -> assertThat(result).isSameAs(saved),
                    () -> assertThat(saved.getState()).isEqualTo(next),
                    () -> assertThat(saved.getUpdatedAt()).isEqualTo(NOW_1)
            );
        }

        @ParameterizedTest
        @CsvSource({
                "IN_USE, INACTIVE",
                "INACTIVE, IN_USE"
        })
        void disallowed(State current, State next) {
            Device device = Device.builder().id(ID_1).state(current).updatedAt(BEF_1).build();
            when(repository.findById(ID_1)).thenReturn(Optional.of(device));

            assertThatThrownBy(() -> service.updateState(ID_1, next))
                    .isInstanceOf(StateException.class);

            verify(repository, never()).save(any(Device.class));
        }

        @Test
        void notFound() {
            when(repository.findById(ID_1)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateState(ID_1, State.AVAILABLE))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ID_1.toString());
        }
    }

    @Nested
    class UpdateDevice {

        static Stream<Arguments> partialUpdates() {
            return Stream.of(
                    arguments(NAME_2, null, NAME_2, BRAND_1),
                    arguments(null, BRAND_2, NAME_1, BRAND_2),
                    arguments(NAME_2, BRAND_2, NAME_2, BRAND_2),
                    arguments(null, null, NAME_1, BRAND_1)
            );
        }

        @ParameterizedTest
        @MethodSource("partialUpdates")
        void partialUpdate(String inputName, String inputBrand, String expectedName, String expectedBrand) {
            Device device = Device.builder()
                    .id(ID_1).name(NAME_1).brand(BRAND_1).state(State.AVAILABLE).updatedAt(BEF_1).build();
            when(repository.findById(ID_1)).thenReturn(Optional.of(device));
            when(timeService.now()).thenReturn(NOW_1);
            when(repository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Device result = service.updateDevice(ID_1, inputName, inputBrand);

            ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);
            verify(repository).save(captor.capture());
            Device saved = captor.getValue();
            assertAll(
                    () -> assertThat(result).isSameAs(saved),
                    () -> assertThat(saved.getName()).isEqualTo(expectedName),
                    () -> assertThat(saved.getBrand()).isEqualTo(expectedBrand),
                    () -> assertThat(saved.getUpdatedAt()).isEqualTo(NOW_1)
            );
        }

        @Test
        void inUseRejected() {
            Device device = Device.builder().id(ID_1).state(State.IN_USE).build();
            when(repository.findById(ID_1)).thenReturn(Optional.of(device));

            assertThatThrownBy(() -> service.updateDevice(ID_1, NAME_2, BRAND_2))
                    .isInstanceOf(ModificationException.class);

            verify(repository, never()).save(any(Device.class));
        }

        @Test
        void notFound() {
            when(repository.findById(ID_1)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateDevice(ID_1, NAME_2, BRAND_2))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ID_1.toString());
        }
    }

}

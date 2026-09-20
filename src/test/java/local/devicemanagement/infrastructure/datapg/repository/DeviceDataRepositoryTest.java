package local.devicemanagement.infrastructure.datapg.repository;

import local.devicemanagement.domain.model.Device;
import local.devicemanagement.domain.model.DeviceFilter;
import local.devicemanagement.domain.model.State;
import local.devicemanagement.infrastructure.datapg.entity.DeviceEntity;
import local.devicemanagement.infrastructure.datapg.mapper.DeviceEntityMapperImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({DeviceDataRepository.class, DeviceEntityMapperImpl.class})
class DeviceDataRepositoryTest {

    private static final Instant NOW_1 = Instant.parse("2026-09-20T08:00:00Z");
    private static final String NAME_1 = "iPhone 15";
    private static final String NAME_2 = "iPad Air";
    private static final String NAME_3 = "Galaxy S24";
    private static final String BRAND_1 = "Apple";
    private static final String BRAND_2 = "Samsung";

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17")
            .withCopyFileToContainer(
                    MountableFile.forHostPath("db/sql/001_create_device_table.sql"),
                    "/docker-entrypoint-initdb.d/001_create_device_table.sql");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private DeviceDataRepository repository;

    @Autowired
    private DeviceCrudRepository crud;

    @BeforeEach
    void seedDevices() {
        crud.deleteAll();
        seed(NAME_1, BRAND_1, State.AVAILABLE);
        seed(NAME_2, BRAND_1, State.IN_USE);
        seed(NAME_3, BRAND_2, State.AVAILABLE);
    }

    private void seed(String name, String brand, State state) {
        crud.save(DeviceEntity.builder()
                .name(name)
                .brand(brand)
                .state(state)
                .createdAt(NOW_1)
                .updatedAt(NOW_1)
                .build());
    }

    @Nested
    class FindAll {

        @Test
        void noFilterReturnsAll() {
            List<Device> result = repository.findAll(new DeviceFilter(null, null, null));

            assertThat(result)
                    .extracting(Device::getName)
                    .containsExactlyInAnyOrder(NAME_1, NAME_2, NAME_3);
        }

        @Test
        void byBrandExact() {
            List<Device> result = repository.findAll(new DeviceFilter(BRAND_1, null, null));

            assertThat(result)
                    .extracting(Device::getName)
                    .containsExactlyInAnyOrder(NAME_1, NAME_2);
        }

        @Test
        void byNamePrefixIgnoresCase() {
            List<Device> result = repository.findAll(new DeviceFilter(null, "ipad", null));

            assertThat(result)
                    .extracting(Device::getName)
                    .containsExactly(NAME_2);
        }

        @Test
        void byNameMatchesPrefixNotSubstring() {
            List<Device> result = repository.findAll(new DeviceFilter(null, "Air", null));

            assertThat(result).isEmpty();
        }

        @Test
        void byState() {
            List<Device> result = repository.findAll(new DeviceFilter(null, null, State.AVAILABLE));

            assertThat(result)
                    .extracting(Device::getName)
                    .containsExactlyInAnyOrder(NAME_1, NAME_3);
        }

        @Test
        void combinedCriteriaAreAnded() {
            List<Device> result = repository.findAll(new DeviceFilter(BRAND_1, "ip", State.IN_USE));

            assertThat(result).hasSize(1);
            Device device = result.getFirst();
            assertAll(
                    () -> assertThat(device.getName()).isEqualTo(NAME_2),
                    () -> assertThat(device.getBrand()).isEqualTo(BRAND_1),
                    () -> assertThat(device.getState()).isEqualTo(State.IN_USE)
            );
        }
    }

}

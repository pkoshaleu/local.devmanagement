package local.devicemanagement.infrastructure.datapg.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import local.devicemanagement.domain.model.State;

import java.time.Instant;


@Table("device")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEntity {

    @Id
    private Integer id;

    @Column("device_name")
    private String name;

    @Column("device_brand")
    private String brand;

    @Column("device_state")
    private State state;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Version
    private Integer version;

}

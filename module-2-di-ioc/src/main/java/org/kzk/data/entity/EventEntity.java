package org.kzk.data.entity;

import lombok.*;
import org.kzk.data.entity.status.EventStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events", schema = "module_2_di_ioc")
public class EventEntity {
    @Id
    @Column()
    private Integer id;

    @Column("user_id")
    private Integer userId;

    @Column("file_id")
    private Integer fileId;

    @Column("status")
    private EventStatus status;

    @Column("timestamp")
    private OffsetDateTime time;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntity event = (EventEntity) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}

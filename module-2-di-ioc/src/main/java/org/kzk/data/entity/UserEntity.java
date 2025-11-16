package org.kzk.data.entity;

import lombok.*;
import org.kzk.data.entity.status.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class UserEntity {

    @Id
    private Integer id;

    @Column("username")
    private String username;

    @Column("status")
    private UserStatus status;

    public boolean isEnabled() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity writer = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), writer.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}

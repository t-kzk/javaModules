package org.kzk.data.entity;

import org.kzk.data.entity.status.UserStatus;
import org.springframework.data.relational.core.mapping.Column;

public record UserAuthEntity(
        @Column("user_id")
        Integer userId,
        String username,
        UserStatus status,
        @Column("password_hash")
        String passwordHash,
        UserRole role
) {
    public boolean isEnabled() {
        return status.equals(UserStatus.ACTIVE);
    }
}

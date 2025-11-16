package org.kzk.dto;

import lombok.Data;
import org.kzk.data.entity.status.UserStatus;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private UserStatus status;
}

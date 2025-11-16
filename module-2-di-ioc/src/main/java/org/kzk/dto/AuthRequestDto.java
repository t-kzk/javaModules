package org.kzk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AuthRequestDto {

    private String username;
    private String password;
}

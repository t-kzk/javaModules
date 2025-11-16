package org.kzk.data.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "auth", schema = "module_2_di_ioc")
public class AuthEntity {
    @Id
    @Column()
    private Integer id;

    @Column("user_id")
    private Integer userId;

    @Column("password_hash")
    private String passwordHash;

    @Column
    private UserRole role;

    @ToString.Include(name = "passwordHash")
    private String maskPasswordHash(){
        return "********";
    }
}

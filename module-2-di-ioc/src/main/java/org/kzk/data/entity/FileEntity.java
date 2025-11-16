package org.kzk.data.entity;

import lombok.*;
import org.kzk.data.entity.status.FileStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files", schema = "module_2_di_ioc")
public class FileEntity {
    @Id
    @Column()
    private Integer id;

    @Column
    private String name;

    @Column()
    private String location;

    @Column
    private FileStatus status;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity file = (FileEntity) o;
        return getId() != null && Objects.equals(getId(), file.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}

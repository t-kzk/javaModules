package org.kzk.model;

import java.time.LocalDateTime;
import java.util.List;

public record Post(Integer id,
                   String content,
                   LocalDateTime created,
                   LocalDateTime updated,
                   List<Label> labels,
                   PostStatus status,
                   Integer writerId) {
}

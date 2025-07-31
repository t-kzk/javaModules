package kzk.test.model;

import java.util.List;
import java.util.UUID;

public record Writer(Integer id,
                     String firstName,
                     String lastName,
                     List<Post> posts) {
}

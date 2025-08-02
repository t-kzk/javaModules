package org.kzk.model;

import java.util.List;

public record Writer(Integer id,
                     String firstName,
                     String lastName,
                     List<Post> posts) {
}

package org.kzk.integration.configuration.junitExtension.annotation;


import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.integration.configuration.junitExtension.preconditions.GenerateFilesExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({GenerateFilesExtension.class})
public @interface GenerateFiles {
    int count () default 0;
}

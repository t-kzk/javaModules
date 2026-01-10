package org.kzk.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .openapi("3.0.1")
                .info(new Info()
                        .title("Module-2 DI & IoC API")
                        .version("1.0")
                        .description("Training backend service"));
    }

}

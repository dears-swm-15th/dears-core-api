package com.teamdears.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", bearerAuth))
                .addSecurityItem(securityRequirement)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Dears API Documentation")
                .description(
                         """
                         Customer | Clara : 51fc7d6b-7f86-43cf-b5c7-de4c46046d71\n
                         Customer | Jeff : ed21f25b-f51c-4e07-b1f5-4ffb2d9a0531\n
                         Wedding Planner | Alice : b1b3825f-304f-4bce-b3b7-91b70fe79cb7\n
                         Wedding Planner | Bob : c14h814f-33gf-4b4e-z5z7-31b70fe74cb8
                         """
                )
                .version("0.1.1");
    }
}

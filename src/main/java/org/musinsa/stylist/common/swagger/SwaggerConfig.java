package org.musinsa.stylist.common.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI stylistAPI() {
        return new OpenAPI()
            .addServersItem(new Server().url("http://localhost:8080").description("local(8080)"))
            .info(new Info()
                      .title("Musinsa Stylist API")
                      .description("Musinsa Stylist API 명세서")
                      .version("1.0.0"));
    }
}

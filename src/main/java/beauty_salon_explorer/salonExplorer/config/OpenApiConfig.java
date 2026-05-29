package beauty_salon_explorer.salonExplorer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI salonExplorerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Warsaw Beauty Salon Explorer API")
                        .version("1.0.0")
                        .description("REST API for browsing, filtering, importing and editing Warsaw beauty salons."));
    }
}
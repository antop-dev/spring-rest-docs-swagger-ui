package org.antop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SwaggerUiConfig implements WebMvcConfigurer {
    public static final String API_DOCS_URL = "/v3/api-docs";
    public static final String API_VERSION = "3.0.3";
    public static final String API_DOCS_PATH = "classpath:/api-docs";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui").setViewName("redirect:/swagger-ui/");
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
    }

    @RestController
    @RequestMapping(value = "/swagger-resources", produces = MediaType.APPLICATION_JSON_VALUE)
    static class SwaggerController implements ResourceLoaderAware {
        private final List<SwaggerResource> resources = new ArrayList<>();

        @GetMapping
        public List<SwaggerResource> swaggerResources() {
            return resources;
        }

        @GetMapping(value = "/configuration/ui")
        public UiConfiguration uiConfiguration() {
            return UiConfigurationBuilder.builder()
                    .build();
        }

        @GetMapping(value = "/configuration/security")
        public SecurityConfiguration securityConfiguration() {
            return SecurityConfigurationBuilder.builder()
                    .build();
        }

        @Override
        public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
            ObjectMapper mapper = new ObjectMapper();
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            try {
                for (Resource resource : resolver.getResources(API_DOCS_PATH + "/*.json")) {
                    JsonNode node = mapper.readTree(Files.readString(Paths.get(resource.getURI())));
                    String title = node.get("info").get("title").asText();

                    SwaggerResource sr = new SwaggerResource();
                    sr.setName(title);
                    sr.setSwaggerVersion(API_VERSION);
                    sr.setLocation(API_DOCS_URL + "/" + resource.getFilename());
                    sr.setUrl(API_DOCS_URL + "/" + resource.getFilename());

                    resources.add(sr);
                }
            } catch (IOException e) {
                log.warn("Failed to load API specification file.", e);
            }
        }
    }

    @RequiredArgsConstructor
    @RestController
    static class OpenApi3Controller {
        private final ResourceLoader resourceLoader;

        @GetMapping(value = API_DOCS_URL + "/{name}.json", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Resource> apiDocs(@PathVariable String name) {
            Resource resource = resourceLoader.getResource(API_DOCS_PATH + "/" + name + ".json");
            if (!resource.exists()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(resource);
        }
    }

}

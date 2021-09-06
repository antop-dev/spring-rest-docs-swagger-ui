package org.antop;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }

    // WebFlux
//    @Bean
//    public RestDocsWebTestClientConfigurationCustomizer restDocsWebTestClientConfigurationCustomizer() {
//        return configurer -> configurer.operationPreprocessors()
//                .withRequestDefaults(prettyPrint())
//                .withResponseDefaults(prettyPrint());
//    }

}

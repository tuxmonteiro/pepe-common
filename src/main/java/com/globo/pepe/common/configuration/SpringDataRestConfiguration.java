package com.globo.pepe.common.configuration;

import com.globo.pepe.common.model.munin.AbstractEntity;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SpringDataRestConfiguration implements RepositoryRestConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        disableEvo(config);
        exposeIdsEntities(config);
        setupCors(config);
    }

    private void disableEvo(RepositoryRestConfiguration config) {
        config.setRelProvider(new DefaultRelProvider());
    }

    private void exposeIdsEntities(RepositoryRestConfiguration config) {
        final Set<BeanDefinition> beans = allBeansDomain();
        for (BeanDefinition bean : beans) {
            try {
                Class<?> idExposedClasses = Class.forName(bean.getBeanClassName());
                config.exposeIdsFor(Class.forName(idExposedClasses.getName()));
            } catch (ClassNotFoundException e) {
                // Can't throw ClassNotFoundException due to the method signature. Need to cast it
                throw new RuntimeException("Failed to expose `id` field due to", e);
            }
        }
    }

    private Set<BeanDefinition> allBeansDomain() {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        return provider.findCandidateComponents(AbstractEntity.class.getPackage().getName());
    }

    private void setupCors(RepositoryRestConfiguration config) {
        String pathPatternCors = "/**";
        config.getCorsRegistry().addMapping(pathPatternCors);
        CorsConfiguration corsConfiguration = config.getCorsRegistry().getCorsConfigurations().get(pathPatternCors);
        corsConfiguration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        corsConfiguration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        corsConfiguration.setAllowCredentials(true);
    }
}

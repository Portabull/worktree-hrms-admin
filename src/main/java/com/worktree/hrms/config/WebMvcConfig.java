package com.worktree.hrms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.io.IOException;


@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${ui.version}")
    private String uiVersion;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Version : {}", uiVersion);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new VersionResourceResolver()
                        .addContentVersionStrategy("/**"))
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {

                        if (resourcePath.endsWith("notfound")) {
                            return location.createRelative("notfound.html");
                        }

                        Resource resource = location.createRelative(resourcePath + ".html");
                        return resource.exists() ? resource : super.getResource(resourcePath, location);
                    }
                });

    }
}

package com.worktree.hrms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.io.IOException;
import java.util.UUID;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String version = UUID.randomUUID().toString();
        System.out.println("Version : " + version);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new VersionResourceResolver().addFixedVersionStrategy(version, "/**"))
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath + ".html");
                        return resource.exists() ? resource : super.getResource(resourcePath, location);
                    }
                });
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/")
//                .resourceChain(false)
//                .addResolver(new PathResourceResolver() {
//                    @Override
//                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
//                        // First, try to resolve the resource with .html suffix
//                        Resource resource = location.createRelative(resourcePath + ".html");
//
//                        // Check if resource exists for paths like /payment-history or /payment-history/1
//                        if (resource.exists() && resource.isReadable()) {
//                            return resource;
//                        } else {
//                            // If the full path with .html doesn't exist, try resolving the base path without dynamic parts
//                            String baseResourcePath = resourcePath.split("/")[0] + ".html";
//                            Resource baseResource = location.createRelative(baseResourcePath);
//                            return baseResource.exists() && baseResource.isReadable() ? baseResource : super.getResource(resourcePath, location);
//                        }
//                    }
//                });
//    }


}

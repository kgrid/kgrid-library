package org.kgrid.library;

import org.kgrid.shelf.repository.CompoundDigitalObjectStore;
import org.kgrid.shelf.repository.CompoundDigitalObjectStoreFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(scanBasePackages = {"org.kgrid.library","org.kgrid.shelf"})
public class LibraryApplication extends WebMvcConfigurerAdapter {

    public LibraryApplication(@Value("${library.name}") String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController( "/" ).setViewName( "forward:/index.html" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
    }

    @Bean
    SimpleInfoContributor getLibraryNameContributor()  {
        return new SimpleInfoContributor("library.name", name);
    }

    private String name;

    public String getLibraryName() {
        return name;
    }

    @Primary
    @Bean
    public static CompoundDigitalObjectStore getCDOStore( @Value("${kgrid.shelf.cdostore.url:filesystem:file://shelf}") String cdoStoreURI) {
        return CompoundDigitalObjectStoreFactory.create(cdoStoreURI);
    }

}

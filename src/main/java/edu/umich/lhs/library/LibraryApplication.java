package edu.umich.lhs.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(scanBasePackages = {"edu.umich.lhs.library","kgrid.org.shelf"})
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

}

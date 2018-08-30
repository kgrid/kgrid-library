package org.kgrid.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("dataSource")
	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private AuthenticationSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
		.and()
		.formLogin()
		.failureHandler(new SimpleUrlAuthenticationFailureHandler())
        .successHandler(successHandler)
	    .and()
	    .logout()
	    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(getUserService());
	}
	
	@Bean
	public CustomizedUserManager getUserService() {
		CustomizedUserManager mg = new CustomizedUserManager();
		mg.setDataSource(dataSource);
		return mg;
	}
}

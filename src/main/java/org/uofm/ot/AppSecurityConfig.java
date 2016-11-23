package org.uofm.ot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private AuthenticationSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(new Http401AuthenticationEntryPoint("Forms realm=\"kgrid\""))
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

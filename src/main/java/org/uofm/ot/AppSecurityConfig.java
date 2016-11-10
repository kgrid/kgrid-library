package org.uofm.ot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
    //   http.authorizeRequests().anyRequest().fullyAuthenticated();
     //    http.httpBasic();
 //       http.csrf().disable();
		
		/*http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/home").permitAll()		
		.anyRequest().fullyAuthenticated().and().formLogin()
				.loginPage("/home").failureUrl("/login?error").permitAll().and()
				.logout().permitAll()
				;*/
		
		// Form authentication is working 
		
		/*http.csrf().disable()
        .httpBasic()
      .and()
        .authorizeRequests()
          .antMatchers("/home", "/", "/login").permitAll()
          .anyRequest().authenticated().and().formLogin();*/
		
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/home", "/").permitAll()
		.and().formLogin()
	    .loginPage("/login").failureUrl("/login?error").permitAll().and()
	    .logout().permitAll();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*auth.inMemoryAuthentication()
		.withUser("admin").password("admin").roles("ADMIN", "USER").and()
		.withUser("user").password("user").roles("USER");*/
		
		/*auth.jdbcAuthentication().dataSource(dataSource).
		withDefaultSchema().withUser("user").password("user").roles("USER");*/
		
		// auth.jdbcAuthentication().dataSource(this.dataSource);
		
		 auth.userDetailsService(getUserService());
	}
	
	@Bean
	public CustomizedUserManager getUserService() {
		CustomizedUserManager mg = new CustomizedUserManager();
		mg.setDataSource(dataSource);
	//	userDetailService = mg;
		return mg;
	}
}

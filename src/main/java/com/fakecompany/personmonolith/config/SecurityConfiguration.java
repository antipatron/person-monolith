package com.fakecompany.personmonolith.config;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;


@Configuration
@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		securedEnabled = true,
		jsr250Enabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_LIST = {
			// -- swagger ui
			"/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html",
			"/webjars/**" };

	@Autowired
	private CORSFilter corsFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests().antMatchers(HttpMethod.GET, AUTH_LIST).permitAll()
				.antMatchers(HttpMethod.POST, "/user/singin/**").permitAll()
				.antMatchers(HttpMethod.POST, "/user/customer/**").permitAll()
				.antMatchers("/").permitAll()
				.anyRequest().permitAll().and()//anyRequest().authenticated().and()
				.addFilterBefore(corsFilter, ChannelProcessingFilter.class)
				.csrf().disable()

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling();
		http.httpBasic().disable();
		http.formLogin().disable();

	}



	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
				"/configuration/security", "/swagger-ui.html", "/webjars/**");
	}

}

package com.andc.amway.datacubecatcher;

import com.andc.amway.datacubecatcher.filter.ApiFilter;
import com.andc.amway.datacubecatcher.service.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class MpDatacubeCatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpDatacubeCatcherApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean MyFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
			registration.setFilter(apiFilter());
			registration.addUrlPatterns("/api/*");
			registration.setName("apiFilter");
			registration.setOrder(1);
		return registration;
	}

	@Bean
	public ApiFilter apiFilter(){
		return new ApiFilter();
	}

	@Autowired
	private RestTemplateBuilder builder;

	@Bean
	public RestTemplate restTemplate() {
		return builder.build();
	}


	@Autowired
	ScheduleManager scheduleManager;

	@Bean
	public CommandLineRunner prepareBuildPlanCalendar(){
		return strings -> scheduleManager.scheduleToBuild();
	}

}

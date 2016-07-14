package com.example.jobprocess.configuration;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityConfiguration {

	@Autowired
	ProcessEngine processEngine;
	
	@Bean
	public ProcessEngine configure(){
		ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
		processEngineConfiguration.setJobExecutorActivate(true);
		processEngineConfiguration.setAsyncExecutorActivate(true);
		return processEngine;
	}
}

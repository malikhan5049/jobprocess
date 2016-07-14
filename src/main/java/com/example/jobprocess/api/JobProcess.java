package com.example.jobprocess.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobprocess.domain.Job;
import com.example.jobprocess.repository.ApplicantRepository;
import com.example.jobprocess.repository.JobRepository;

@RestController
public class JobProcess {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
    private TaskService taskService;
	
	@ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/start-job-process", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void startJobProcess(@RequestBody Map<String, String> data) throws InterruptedException {

        /*Job job = new Job(data.get("jobTitle"), data.get("jobDescription"));
        jobRepository.save(job);

        Map<String, Object> vars = Collections.<String, Object>singletonMap("job", job);
        runtimeService.startProcessInstanceByKey("jobProcess", vars);*/
		
		Job job = createNSaveTestJob();
    	Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("job", job);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jobProcess", variables);
        Task task = proceedUptoPublishJob(processInstance, variables);
        
        variables.put("publish", "true");
        variables.put("jobCloseDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        taskService.complete(task.getId(), variables);
        
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals("Wait for Applicants", task.getName());
        Thread.currentThread().join(TimeUnit.SECONDS.toMillis(5));
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals("Short List Applicants", task.getName());
    }
	
	
	@ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/start-job-process-get", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public void startJobProcessGet() throws InterruptedException {

        /*Job job = new Job(data.get("jobTitle"), data.get("jobDescription"));
        jobRepository.save(job);

        Map<String, Object> vars = Collections.<String, Object>singletonMap("job", job);
        runtimeService.startProcessInstanceByKey("jobProcess", vars);*/
		
		Job job = createNSaveTestJob();
    	Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("job", job);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jobProcess", variables);
        Task task = proceedUptoPublishJob(processInstance, variables);
        
        variables.put("publish", "true");
        variables.put("jobCloseDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        taskService.complete(task.getId(), variables);
        
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//        Assert.assertEquals("Wait for Applicants", task.getName());
        Thread.currentThread().join(TimeUnit.SECONDS.toMillis(5));
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals("Short List Applicants", task.getName());
    }

	private Task proceedUptoPublishJob(ProcessInstance processInstance, Map<String, Object> variables) {
		Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
//        Assert.assertEquals("Define Job", task.getName());
        variables.clear();
        
		taskService.complete(task.getId());
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//        Assert.assertEquals("Publish Job", task.getName());
        return task;
	}

	private Job createNSaveTestJob() {
		Job job = new Job("Senior Java Developer", "Senior Java Developer is required");
        jobRepository.save(job);
		return job;
	}

}

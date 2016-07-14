package com.example.jobprocess;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.jobprocess.configuration.ActivityConfiguration;
import com.example.jobprocess.domain.Job;
import com.example.jobprocess.repository.JobRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {JobprocessApplication.class, ActivityConfiguration.class})
@WebAppConfiguration
@IntegrationTest
public class JobProcessTest{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private JobRepository jobRepository;

   
    @Before
    public void setup() {
    }

    @After
    public void cleanup() {
    }
    
    
    @Test
    public void testStartJobProcess_jobPublished_waitForApplicants_timeElapsed() throws Exception{
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

    @Test
    public void testStartJobProcess_jobNotPublished() {

        Job job = createNSaveTestJob();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("job", job);
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jobProcess", variables);
    	
        Task task = proceedUptoPublishJob(processInstance, variables);
        
        variables.put("publish", "false");
        taskService.complete(task.getId(), variables);
        
        Assert.assertEquals(1, historyService.createHistoricProcessInstanceQuery().finished().count());
        
        

        /*// Completing the phone interview with success should trigger two new tasks
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("telephoneInterviewOutcome", true);
        taskService.complete(task.getId(), taskVariables);

        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .orderByTaskName().asc()
                .list();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals("Financial negotiation", tasks.get(0).getName());
        Assert.assertEquals("Tech interview", tasks.get(1).getName());

        // Completing both should wrap up the subprocess, send out the 'welcome mail' and end the process instance
        taskVariables = new HashMap<String, Object>();
        taskVariables.put("techOk", true);
        taskService.complete(tasks.get(0).getId(), taskVariables);

        taskVariables = new HashMap<String, Object>();
        taskVariables.put("financialOk", true);
        taskService.complete(tasks.get(1).getId(), taskVariables);

        // Verify email
        Assert.assertEquals(1, wiser.getMessages().size());

        // Verify process completed
        Assert.assertEquals(1, historyService.createHistoricProcessInstanceQuery().finished().count());*/

    }

	private Job createNSaveTestJob() {
		Job job = new Job("Senior Java Developer", "Senior Java Developer is required");
        jobRepository.save(job);
		return job;
	}

	private Task proceedUptoPublishJob(ProcessInstance processInstance, Map<String, Object> variables) {
		Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        Assert.assertEquals("Define Job", task.getName());
        variables.clear();
        
		taskService.complete(task.getId());
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals("Publish Job", task.getName());
        return task;
	}

}

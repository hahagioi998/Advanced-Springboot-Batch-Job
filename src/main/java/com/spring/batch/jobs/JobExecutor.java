package com.spring.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.spring.batch.models.User;
import com.spring.batch.processors.Processor;
import com.spring.batch.readers.Reader;
import com.spring.batch.repositories.UserRepository;
import com.spring.batch.writers.Writer;

@Component
public class JobExecutor extends JobExecutionListenerSupport {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Value("${input.file}")
	Resource resource;

	@Autowired
	Processor processor;

	@Autowired
	Writer writer;
	
	@Autowired
	UserRepository repository;
	
	Logger logger = LoggerFactory.getLogger(JobExecutor.class);

	@Bean(name = "accountJob")
	public Job accountKeeperJob() {

		Step step = stepBuilderFactory.get("step-1").<User, User>chunk(1).reader(new Reader(resource))
				.processor(processor).writer(writer).build();

		Job job = jobBuilderFactory.get("accounting-job").incrementer(new RunIdIncrementer()).listener(this).start(step)
				.build();

		return job;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("BATCH JOB COMPLETED SUCCESSFULLY");
			repository.findAll().forEach(user->{
				logger.info("User: " + user);
			});
		}
	}
}

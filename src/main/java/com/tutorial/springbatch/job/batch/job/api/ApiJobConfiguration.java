package com.tutorial.springbatch.job.batch.job.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.springbatch.job.batch.listener.JobListener;
import com.tutorial.springbatch.job.batch.tasklet.ApiEndTasklet;
import com.tutorial.springbatch.job.batch.tasklet.ApiStartTasklet;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfiguration {

	private final ApiStartTasklet apiStartTasklet;
	private final ApiEndTasklet apiEndTasklet;
	private final Step jobStep;

	@Bean
	public Job apiJob(JobRepository jobRepository, Step apiStep1, Step apiStep2) {
		return new JobBuilder("apiJob",jobRepository)
			.listener(new JobListener())
			.start(apiStep1)
			.next(jobStep)
			.next(apiStep2)
			.build();
	}

	@JobScope
	@Bean
	public Step apiStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("apiStep1",jobRepository)
			.tasklet(apiStartTasklet, transactionManager)
			.build();
	}

	@JobScope
	@Bean
	public Step apiStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("apiStep2",jobRepository)
			.tasklet(apiEndTasklet, transactionManager)
			.build();
	}
}

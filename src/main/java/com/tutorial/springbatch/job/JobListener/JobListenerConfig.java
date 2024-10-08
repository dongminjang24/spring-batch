package com.tutorial.springbatch.job.JobListener;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JobListenerConfig {


	@Bean
	public Job jobListenerJob(JobRepository jobRepository, Step jobListenerStep) {
		return new JobBuilder("jobListenerJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(new JobLoggerListener())
			.start(jobListenerStep)
			.build();
	}

	@JobScope
	@Bean
	public Step jobListenerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("jobListenerStep", jobRepository)
			.tasklet(jobListenerTasklet(), transactionManager)
			.build();
	}


	@StepScope
	@Bean
	public Tasklet jobListenerTasklet(){
		return (contribution, chunkContext) -> {
			log.info("Tasklet 시작");
			// throw new Exception("강제 에러 발생");
			return RepeatStatus.FINISHED;
		};
	}

}

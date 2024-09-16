package com.tutorial.springbatch.job.ValidatedParam;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.springbatch.job.ValidatedParam.Validator.FileParamValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ValidatedParamJobConfig {

	@Bean
	public Job validatedParamJob(JobRepository jobRepository, Step validatedParamStep) {
		return new JobBuilder("validatedParamJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.validator(new FileParamValidator())
			.start(validatedParamStep)
			.build();
	}

	@JobScope
	@Bean
	public Step validatedParamStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet validatedParamTasklet) {
		return new StepBuilder("validatedParamStep", jobRepository)
			.tasklet(validatedParamTasklet, transactionManager)
			.build();
	}


	@StepScope
	@Bean
	public Tasklet validatedParamTasklet(@Value("#{jobParameters['-fileName']}") String fileName){
		return (contribution, chunkContext) -> {
			log.info("fileName = {}", fileName);
			return RepeatStatus.FINISHED;
		};
	}
}

package com.tutorial.springbatch.job.MultipleStep;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultipleConditionalStepJobConfig {


	@Bean
	public Job conditionalStepJob(JobRepository jobRepository,
		Step conditionalStartStep,
		Step conditionalAllStep,
		Step conditionalFailStep,
		Step conditionalCompleteStep) {
		return new JobBuilder("conditionalStepJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(conditionalStartStep)
			.on("FAILED").to(conditionalFailStep)
			.from(conditionalStartStep)
			.on("COMPLETED").to(conditionalCompleteStep)
			.from(conditionalStartStep)
			.on("*").to(conditionalAllStep) // 그외 모든 경우
			.end()
			.build();
	}

	@JobScope
	@Bean
	public Step conditionalStartStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("conditionalStartStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("conditionalStartStep");
				// throw new Exception("강제 에러 발생");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@JobScope
	@Bean
	public Step conditionalAllStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("conditionalAllStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("conditionalAllStep");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@JobScope
	@Bean
	public Step conditionalFailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("conditionalFailStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("conditionalFailStep");

				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
	@JobScope
	@Bean
	public Step conditionalCompleteStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("conditionalCompleteStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("conditionalCompleteStep");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}


}

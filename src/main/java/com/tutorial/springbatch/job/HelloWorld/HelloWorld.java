// package com.tutorial.springbatch.job.HelloWorld;
//
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.configuration.annotation.JobScope;
// import org.springframework.batch.core.configuration.annotation.StepScope;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.launch.support.RunIdIncrementer;
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.core.step.tasklet.Tasklet;
// import org.springframework.batch.repeat.RepeatStatus;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Configuration
// public class HelloWorld {
//
// 	@Bean
// 	public Job helloWorldJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
// 		return new JobBuilder("helloWorldJob", jobRepository)
// 			.incrementer(new RunIdIncrementer())
// 			.start(helloWorldStep(jobRepository, transactionManager))
// 			.build();
// 	}
//
// 	@JobScope
// 	@Bean
// 	public Step helloWorldStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
// 		return new StepBuilder("helloWorldStep", jobRepository)
// 			.tasklet(helloWorldTasklet(), transactionManager)  // 트랜잭션 매니저 추가
// 			.build();
// 	}
//
// 	@StepScope
// 	@Bean
// 	public Tasklet helloWorldTasklet(){
// 		return (contribution, chunkContext) -> {
// 			log.info("Tasklet 시작");
// 			log.info("안녕하세요");
// 			log.info("Tasklet 완료");
// 			return RepeatStatus.FINISHED;
// 		};
// 	}
// }

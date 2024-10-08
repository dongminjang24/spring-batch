// package com.tutorial.springbatch.job;
//
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.JobParametersBuilder;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
//
// import lombok.RequiredArgsConstructor;
//
// @Component
// @RequiredArgsConstructor
// public class JobRunner implements CommandLineRunner {
//
// 	private final JobLauncher jobLauncher;
//
// 	private final Job helloWorldJob;
//
// 	@Override
// 	public void run(String... args) throws Exception {
// 		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
// 		jobLauncher.run(helloWorldJob, jobParameters);
// 	}
// }

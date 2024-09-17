package com.tutorial.springbatch.job.scheduling;



import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SampleSchedule {

	private final Job helloWorldJob;

	private final JobLauncher jobLauncher;


	@Scheduled(cron = "0 */1 * * * *")
	public void helloWorldJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("requestTime", System.currentTimeMillis())
			.toJobParameters();


		jobLauncher.run(helloWorldJob, jobParameters);
	}

}

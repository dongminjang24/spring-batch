package com.tutorial.springbatch.job.JobListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {
	private static String BEFORE_MESSAGE = "{} Job is Running";
	private static String AFTER_MESSAGE = "{} Job is Finished with status {}";

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

		if (jobExecution.getStatus().isUnsuccessful()) {
			log.error("Job is failed");
		}
	}
}

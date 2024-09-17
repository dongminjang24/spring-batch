package com.tutorial.springbatch.job.batch.listener;

import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

	@Override
	public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
		System.out.println("JobListener beforeJob executed");
	}

	@Override
	public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
		System.out.println("JobListener afterJob executed");
	}
}

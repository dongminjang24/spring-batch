package com.tutorial.springbatch.job.HelloWorld;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBatchTest
@SpringBootTest(classes = { HelloWorldJobConfig.class, SpringBatchTestConfig.class })
class HelloWorldJobConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void test_helloWorldJob() throws Exception {
		// given
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}


}
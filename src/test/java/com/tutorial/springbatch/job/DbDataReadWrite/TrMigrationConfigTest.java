package com.tutorial.springbatch.job.DbDataReadWrite;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import com.tutorial.springbatch.job.HelloWorld.SpringBatchTestConfig;
import com.tutorial.springbatch.job.core.domain.accounts.AccountsRepository;
import com.tutorial.springbatch.job.core.domain.order.Orders;
import com.tutorial.springbatch.job.core.domain.order.OrdersRepository;

@ActiveProfiles("test")
@SpringBatchTest
@EnableJpaRepositories(basePackages = "com.tutorial.springbatch.job.core.domain")
@EntityScan(basePackages = "com.tutorial.springbatch.job.core.domain")
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class})
class TrMigrationConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private AccountsRepository accountsRepository;

	@AfterEach
	public void cleanUpEach() {
		ordersRepository.deleteAll();
		accountsRepository.deleteAll();
	}

	@Test
	public void success_noData() throws Exception {
		// when
		JobExecution execution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
		Assertions.assertEquals(0, accountsRepository.count());
	}

	@Test
	public void success_existData() throws Exception {
		// given
		Orders orders1 = new Orders(null, "kakao gift", 15000, new Date());
		Orders orders2 = new Orders(null, "naver gift", 15000, new Date());

		ordersRepository.save(orders1);
		ordersRepository.save(orders2);

		// when
		JobExecution execution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
		Assertions.assertEquals(2, accountsRepository.count());
	}
}
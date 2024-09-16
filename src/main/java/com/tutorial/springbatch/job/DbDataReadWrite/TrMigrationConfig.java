package com.tutorial.springbatch.job.DbDataReadWrite;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.springbatch.job.JobListener.JobLoggerListener;
import com.tutorial.springbatch.job.core.domain.accounts.Accounts;
import com.tutorial.springbatch.job.core.domain.accounts.AccountsRepository;
import com.tutorial.springbatch.job.core.domain.order.Orders;
import com.tutorial.springbatch.job.core.domain.order.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

	private final OrdersRepository ordersRepository;
	private final AccountsRepository accountRepository;

	@Bean
	public Job trMigrationJob(JobRepository jobRepository, Step trMigrationStep) {
		return new JobBuilder("trMigrationJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(trMigrationStep)
			.build();
	}

	@JobScope
	@Bean
	public Step trMigrationStep(
		RepositoryItemReader<Orders> trOrdersReader,
		ItemProcessor<Orders, Accounts> trOrdersProcessor,
		ItemWriter<Accounts> trAccountsWriter,
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager) {

		return new StepBuilder("trMigrationStep", jobRepository)
			.<Orders, Accounts>chunk(5, transactionManager)
			.reader(trOrdersReader)
			.processor(trOrdersProcessor)
			.writer(trAccountsWriter)
			.build();
	}

	@StepScope
	@Bean
	public ItemWriter<Accounts> trOrdersWriter() {
		return new ItemWriter<Accounts>() {
			@Override
			public void write(Chunk<? extends Accounts> chunk) throws Exception {
				chunk.getItems().forEach(accountRepository::save);
			}
		};
	}

	// @StepScope
	// @Bean
	// public RepositoryItemWriter<Accounts> trOrdersWriter() {
	// 	return new RepositoryItemWriterBuilder<Accounts>()
	// 		.repository(accountRepository)
	// 		.methodName("save")
	// 		.build();
	// }

	@StepScope
	@Bean
	public ItemProcessor<Orders,Accounts> trMigrationProcessor() {
		return new ItemProcessor<Orders, Accounts>() {
			@Override
			public Accounts process(Orders item) throws Exception {
				return new Accounts(item);
			}
		};
	}

	@StepScope
	@Bean
	public RepositoryItemReader<Orders> trOrdersReader() {
		return new RepositoryItemReaderBuilder<Orders>()
			.name("trOrderReader")
			.repository(ordersRepository)
			.methodName("findAll")
			.pageSize(5)
			.sorts(Collections.singletonMap("id", Sort.Direction.ASC))
			.build();
	}

}

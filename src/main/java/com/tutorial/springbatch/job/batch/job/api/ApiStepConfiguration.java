package com.tutorial.springbatch.job.batch.job.api;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.springbatch.job.batch.domain.dto.ProductVO;
import com.tutorial.springbatch.job.batch.partition.ProductPartitioner;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

	private final DataSource dataSource;

	private final int chunkSize = 10;

	@Bean
	public Step apiMasterStep(JobRepository jobRepository) {
		return new StepBuilder("apiMasterStep", jobRepository)
			.partitioner(apiSlaveStep(jobRepository).getName(), partitioner())
			.step(apiSlaveStep())
			.gridSize(10)
			.taskExecutor(taskExecutor())
			.build();
	}

	@Bean
	public Step apiSlaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws
		Exception {
		return new StepBuilder("apiSlaveStep",jobRepository)
			.<ProductVO,ProductVO> chunk(chunkSize,transactionManager)
			.reader(itemReader(null))
			.processor(ItemProcessor())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ProductPartitioner partitioner() {
		ProductPartitioner productPartitioner = new ProductPartitioner();
		productPartitioner.setDataSource(dataSource);
		return productPartitioner;
	}

	@StepScope
	@Bean
	public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {

		JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(chunkSize);
		reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));

		MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
		mySqlPagingQueryProvider.setSelectClause("id, name, price, type");
		mySqlPagingQueryProvider.setFromClause("from product");
		mySqlPagingQueryProvider.setWhereClause("where type = :type");

		HashMap<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.DESCENDING);
		mySqlPagingQueryProvider.setSortKeys(sortKeys);

		reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
		reader.setQueryProvider(mySqlPagingQueryProvider);
		reader.afterPropertiesSet();

		return reader;
	}


}

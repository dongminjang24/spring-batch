package com.tutorial.springbatch.job.batch.job.file;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.springbatch.job.batch.chunk.processor.FileItemProcessor;
import com.tutorial.springbatch.job.batch.domain.Product;
import com.tutorial.springbatch.job.batch.domain.ProductRepository;
import com.tutorial.springbatch.job.batch.domain.dto.ProductVO;
import com.tutorial.springbatch.job.core.domain.accounts.Accounts;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FileJobConfiguration {

	private final ProductRepository productRepository;

	@Bean
	public Job fileJob(JobRepository jobRepository, Step fileStep) {
		return new JobBuilder("fileJob",jobRepository)
			.start(fileStep)
			.build();
	}

	@JobScope
	@Bean
	public Step fileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("fileStep",jobRepository)
			.<ProductVO, Product>chunk(10,transactionManager)
			.reader(fileItemReader(null))
			.processor(fileItemProcessor())
			.writer(fileItemWriter())
			.build();
	}

	@StepScope
	@Bean
	public FlatFileItemReader<ProductVO> fileItemReader(@Value("#{jobParameters['requestDate']}") String requestDate) {
		return new FlatFileItemReaderBuilder<ProductVO>()
			.name("flatFile")
			.resource(new ClassPathResource("product_"+ requestDate +".csv"))
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
			.targetType(ProductVO.class)
			.linesToSkip(1)
			.delimited().delimiter(",")
			.names(new String[]{"id","name","price","type"})
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<ProductVO,Product> fileItemProcessor() {
		return new FileItemProcessor();
	}


	@StepScope
	@Bean
	public ItemWriter<Product> fileItemWriter() {
		return new ItemWriter<Product>() {
			@Override
			public void write(Chunk<? extends Product> chunk) throws Exception {
				chunk.getItems().forEach(productRepository::save);
			}
		};
	}
}

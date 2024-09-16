package com.tutorial.springbatch.job.FileDataReadWrite;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.springbatch.job.FileDataReadWrite.dto.Player;
import com.tutorial.springbatch.job.FileDataReadWrite.dto.PlayerYears;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

	@Bean
	public Job fileReadWriteJob(JobRepository jobRepository, Step fileReadWriteStep) {
		return new JobBuilder("fileReadWriteJob",jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(fileReadWriteStep)
			.build();
	}

	@JobScope
	@Bean
	public Step fileReadWriteStep(ItemReader<Player> playerItemReader,
		ItemProcessor<Player, PlayerYears> playerProcessor,
		ItemWriter<PlayerYears> playerItemWriter,
		JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("fileReadWriteStep",jobRepository)
			// .tasklet(fileReadWriteTasklet())
			.<Player, PlayerYears>chunk(5,transactionManager)
			.reader(playerItemReader)
			// .writer((ItemWriter)chunk -> chunk.getItems().forEach(System.out::println))
			.processor(playerProcessor)
			.writer(playerItemWriter)
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<Player, PlayerYears> playerProcessor() {
		return PlayerYears::new;
	}

	@StepScope
	@Bean
	public FlatFileItemWriter<PlayerYears> playerItemWriter() {
		BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] {"id", "lastName","position","yearsExperience"});
		fieldExtractor.afterPropertiesSet();

		DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");
		lineAggregator.setFieldExtractor(fieldExtractor);

		FileSystemResource resource = new FileSystemResource("player_output.txt");

		return new FlatFileItemWriterBuilder<PlayerYears>()
			.name("playerItemWriter")
			.resource(resource)
			.lineAggregator(lineAggregator)
			.build();
	}

	@StepScope
	@Bean
	public FlatFileItemReader<Player> playerItemReader() {
		return new FlatFileItemReaderBuilder<Player>()
			.name("playerItemReader")
			.resource(new ClassPathResource("player.csv"))
			.lineTokenizer(new DelimitedLineTokenizer()) // 콤마 단위로 나눠주기 때문에
			.fieldSetMapper(new PlayerFieldSetMapper())
			// .linesToSkip(1) // 첫번째 줄은 건너뛰기
			.build();
	}

}

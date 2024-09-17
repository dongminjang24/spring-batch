package com.tutorial.springbatch.job.batch.partition;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


public class ProductPartitioner implements Partitioner {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		return null;
	}
}

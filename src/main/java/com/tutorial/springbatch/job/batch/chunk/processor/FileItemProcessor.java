package com.tutorial.springbatch.job.batch.chunk.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

import com.tutorial.springbatch.job.batch.domain.Product;
import com.tutorial.springbatch.job.batch.domain.dto.ProductVO;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

	@Override
	public Product process(ProductVO item) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		Product product = modelMapper.map(item, Product.class);
		System.out.println("Processed product: " + product);
		return product;
	}
}

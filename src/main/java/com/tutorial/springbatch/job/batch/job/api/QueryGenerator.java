package com.tutorial.springbatch.job.batch.job.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tutorial.springbatch.job.batch.domain.dto.ProductVO;
import com.tutorial.springbatch.job.batch.rowmapper.ProductRowMapper;

public class QueryGenerator {

	public static ProductVO[] getProductList(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<ProductVO> productVOList = jdbcTemplate.query("select type from product group by type", new ProductRowMapper() {
			@Override
			public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProductVO product = new ProductVO();
				product.setType(rs.getString("type"));
				return product;
			}
		});

		return productVOList.toArray(new ProductVO[]{});
	}

	public static Map<String,Object> getParameterForQuery(String parameter,String value) {
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put(parameter, value);
		return parameters;
	}
}

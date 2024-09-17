package com.tutorial.springbatch.job.batch.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tutorial.springbatch.job.batch.domain.dto.ProductVO;

public class ProductRowMapper implements RowMapper<ProductVO> {

	@Override
	public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductVO product = new ProductVO();
		product.setId(rs.getLong("id"));
		product.setName(rs.getString("name"));
		product.setPrice(rs.getInt("price"));
		product.setType(rs.getString("type"));
		return product;
	}

}

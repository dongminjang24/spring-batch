package com.tutorial.springbatch.job.core.domain.accounts;

import java.util.Date;

import com.tutorial.springbatch.job.core.domain.order.Orders;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Accounts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String orderItem;

	private Integer price;

	private Date orderDate;

	private Date accountDate;

	public Accounts(Orders orders) {
		this.orderItem = orders.getOrderItem();
		this.price = orders.getPrice();
		this.orderDate = orders.getOrderDate();
		this.accountDate = new Date();
	}

}

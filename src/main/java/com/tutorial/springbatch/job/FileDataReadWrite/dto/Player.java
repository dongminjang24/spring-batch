package com.tutorial.springbatch.job.FileDataReadWrite.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Player implements Serializable {
	private String id;

	private String lastName;

	private String firstName;

	private String position;

	private int birthYear;

	private int debutYear;

}

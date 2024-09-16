package com.tutorial.springbatch.job.FileDataReadWrite.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerYears implements Serializable {
	private String id;

	private String lastName;

	private String firstName;

	private String position;

	private int birthYear;

	private int debutYear;

	private int yearsExperience;

	public PlayerYears(Player player) {
		this.id = player.getId();
		this.lastName = player.getLastName();
		this.firstName = player.getFirstName();
		this.position = player.getPosition();
		this.birthYear = player.getBirthYear();
		this.debutYear = player.getDebutYear();
		this.yearsExperience = player.getDebutYear() - player.getBirthYear();
	}
}

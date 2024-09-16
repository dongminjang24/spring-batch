package com.tutorial.springbatch.job.ValidatedParam.Validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class FileParamValidator implements JobParametersValidator {
	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		assert parameters != null;
		String fileName = parameters.getString("-fileName");

		if (fileName == null || !fileName.contains("csv")) {
			throw new JobParametersInvalidException("fileName parameter is missing or not a csv file");
		}
	}


}

package com.cd.uap.utils;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.cd.uap.bean.User;
import com.cd.uap.exception.ValidateException;

public class ValidatedUtils {
	/**
	 * controller调用
	 * 
	 * @param result
	 * @throws ValidateException
	 */
	public static void validate(BindingResult result) throws ValidateException {
		if (result.hasErrors()) {
			String msg = "";
			List<ObjectError> errorList = result.getAllErrors();
			FieldError fieldError = null;
			for (ObjectError error : errorList) {
				if (error instanceof FieldError)
					fieldError = (FieldError) error;
				msg = msg + fieldError.getDefaultMessage() + "  ";
			}
			throw new ValidateException(msg);
		}
	}

	public static void validateObj(Object obj) throws ValidateException {
		// 校验用户
		Set<ConstraintViolation<Object>> set = Validation.buildDefaultValidatorFactory().getValidator().validate(obj);
		String msg = "";
		if (set != null && set.size() > 0) {
			for (ConstraintViolation<Object> constraintViolation : set) {
				msg = msg + constraintViolation.getMessage() + "  ";
			}
			throw new ValidateException(msg);
		}

	}

}

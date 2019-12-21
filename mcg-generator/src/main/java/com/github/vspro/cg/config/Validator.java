package com.github.vspro.cg.config;

import com.github.vspro.cg.exception.InvalidException;

public interface Validator {

	void validate() throws InvalidException;
}

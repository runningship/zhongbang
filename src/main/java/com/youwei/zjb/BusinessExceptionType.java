package com.youwei.zjb;

import org.bc.sdak.ExceptionType;

public enum BusinessExceptionType implements ExceptionType{
	MethodReturnTypeError,
	ModuleInvokeError,
	MethodParameterError,
	ParameterMissingError,
	AuthCodeError,
	MachineCodeEmpty
}

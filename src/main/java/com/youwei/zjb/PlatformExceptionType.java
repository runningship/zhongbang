package com.youwei.zjb;

import org.bc.sdak.ExceptionType;

public enum PlatformExceptionType implements ExceptionType{
	MethodReturnTypeError,
	ModuleInvokeError,
	MethodParameterError,
	ParameterMissingError,
	AuthCodeError,
	MachineCodeEmpty,
	BusinessException
}

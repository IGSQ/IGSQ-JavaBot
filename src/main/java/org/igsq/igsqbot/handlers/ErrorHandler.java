package org.igsq.igsqbot.handlers;

import org.slf4j.LoggerFactory;

public class ErrorHandler
{
	public ErrorHandler(Exception exception)
	{
		LoggerFactory.getLogger(ErrorHandler.class).error("An exception occurred.", exception);
	}
}

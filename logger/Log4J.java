package logger;

import org.apache.log4j.Logger;

public class Log4J
{
	
	private static final Logger log = Logger.getLogger(Log4J.class);

	public static Logger getLog()
	{
		return log;
	}
	

}

/*
 * 3D City Database - The Open Source CityGML Database
 * http://www.3dcitydb.org/
 * 
 * Copyright 2013 - 2017
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 * 
 * virtualcitySYSTEMS GmbH, Berlin <http://www.virtualcitysystems.de/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.citygml4j.tools.common.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {
	private static Logger instance = new Logger();

	private LogLevel consoleLogLevel = LogLevel.INFO;
	private AtomicInteger warnings = new AtomicInteger(0);
	private AtomicInteger errors = new AtomicInteger(0);

	private Logger() {
		// just to thwart instantiation
	}

	public static Logger getInstance() {
		return instance;
	}

	public void setLogLevel(LogLevel consoleLogLevel) {
		this.consoleLogLevel = consoleLogLevel;
	}

	public LogLevel getLogLevel() {
		return consoleLogLevel;
	}

	private String getPrefix(LogLevel type) {
		return "[" +
				LocalDateTime.now().withNano(0).format(DateTimeFormatter.ISO_LOCAL_TIME) +
				" " +
				type +
				"] ";
	}

	private void log(LogLevel type, String msg) {
		if (consoleLogLevel.ordinal() >= type.ordinal())
			System.out.println(getPrefix(type) + msg);
	}

	public void debug(String msg) {		
		log(LogLevel.DEBUG, msg);
	}

	public void info(String msg) {
		log(LogLevel.INFO, msg);
	}

	public void warn(String msg) {
		log(LogLevel.WARN, msg);
		warnings.incrementAndGet();
	}

	public void error(String msg) {
		log(LogLevel.ERROR, msg);
		errors.incrementAndGet();
	}

	public void error(String msg, Throwable e) {
		log(LogLevel.ERROR, msg);
		errors.incrementAndGet();

		do {
			if (e.getMessage() != null)
				log(LogLevel.ERROR, "Cause: " + e.getClass().getName() + ": " + e.getMessage());
		} while ((e = e.getCause()) != null);
	}

	public void logStackTrace(Throwable e) {
		e.printStackTrace(System.out);
	}

	public void print(String msg) {
		System.out.println(msg);
	}

	public int getNumberOfErrors() {
		return errors.get();
	}

	public int getNumberOfWarnings() {
		return warnings.get();
	}

}
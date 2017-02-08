package com.cec.sbs.helper;

import java.io.IOException;
import java.util.Properties;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@ControllerAdvice
public class ControllerHelper {

	public static final String DEBUG = "debug";
	public static final String GIT_COMMIT_ID = "gitCommitId";

	private static final String GIT_PROPERTY_FILE = "git.properties";
	private static Properties properties = new Properties();

	public ControllerHelper() throws IOException {
		properties.load(getClass().getClassLoader().getResourceAsStream(GIT_PROPERTY_FILE));
	}

	@ModelAttribute(DEBUG)
	public Boolean isDebug(@RequestParam(name = DEBUG, required = false, defaultValue = "false") String debug) {

		return Boolean.valueOf(debug);
	}

	@ModelAttribute(GIT_COMMIT_ID)
	public String getGitCommitId() {
		return properties.getProperty("git.commit.id.abbrev");
	}
}

package com.yunus.research;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@SpringBootApplication
public class ResearchApplication {

	public static void main(String[] args) {
		loadDotenv();
		SpringApplication.run(ResearchApplication.class, args);
	}

	private static void loadDotenv() {
		try {
			File envFile = new File(".env");
			if (!envFile.exists()) {
				envFile = new File("../.env");
			}
			if (envFile.exists() && envFile.isFile()) {
				Properties props = new Properties();
				try (FileInputStream fis = new FileInputStream(envFile)) {
					props.load(fis);
				}
				for (String key : props.stringPropertyNames()) {
					if (System.getProperty(key) == null && System.getenv(key) == null) {
						String val = props.getProperty(key);
						if (val != null) {
							val = val.trim();
							if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
								val = val.substring(1, val.length() - 1);
							}
							System.setProperty(key, val);
						}
					}
				}
			}
		} catch (Exception ignored) {
		}
	}

}

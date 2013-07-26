package com.sbezboro.standardplugin.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class LogWriter {
	protected StandardPlugin plugin;

	private BufferedWriter writer;
	private SimpleDateFormat formatter;

	public LogWriter(StandardPlugin plugin) {
		this.plugin = plugin;

		try {
			File logFile = new File(plugin.getDataFolder(), getFilename());
			writer = new BufferedWriter(new FileWriter(logFile, true));
		} catch (IOException e) {
			e.printStackTrace();
		}

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public void unload() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public void log(String message) {
		try {
			writer.write(getDate() + " [INFO] " + message);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
		}
	}

	public String getDate() {
		return formatter.format(new Date());
	}

	public abstract String getFilename();
}

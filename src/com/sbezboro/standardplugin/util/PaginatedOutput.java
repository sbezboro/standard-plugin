package com.sbezboro.standardplugin.util;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.Math;import java.lang.String;import java.util.ArrayList;

public class PaginatedOutput {
	private static final int PAGE_SIZE = 9;

	private ArrayList<String> lines;
	private String header;
	private int page;

	public PaginatedOutput(String header, int page) {
		this.header = header;
		this.page = page;

		this.lines = new ArrayList<String>();
	}

	public void addLine(String line) {
		lines.add(line);
	}

	public void show(CommandSender sender) {
		int numPages = (lines.size() - 1) / PAGE_SIZE + 1;

		if (page < 1 || page > numPages) {
			page = 1;
		}

		int start = (page - 1) * PAGE_SIZE;
		int end = Math.min(start + PAGE_SIZE, lines.size());

		sender.sendMessage(ChatColor.GOLD + "============== " + ChatColor.YELLOW + header + " (" + page + "/" + numPages + ")" + ChatColor.GOLD + " ==============");
		for (int i = start; i < end; ++i) {
			sender.sendMessage(lines.get(i));
		}
	}
}

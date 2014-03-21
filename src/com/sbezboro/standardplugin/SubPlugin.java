package com.sbezboro.standardplugin;

import java.util.List;

import org.bukkit.plugin.Plugin;

import com.sbezboro.standardplugin.commands.ICommand;

public interface SubPlugin extends Plugin {
	public String getSubPluginName();
	public List<ICommand> getCommands();
	public void reloadPlugin();
}

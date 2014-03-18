package com.sbezboro.standardplugin;

import java.util.List;

import com.sbezboro.standardplugin.commands.ICommand;

public interface SubPlugin {
	public String getSubPluginName();
	public List<ICommand> getCommands();
}

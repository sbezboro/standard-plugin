package com.sbezboro.standardplugin.tasks;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.SubPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UUIDMigrationTask extends BaseTask {

	public UUIDMigrationTask(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		HttpProfileRepository repository = new HttpProfileRepository("minecraft");

		List<String> usernames = new ArrayList<String>();

		File directory = new File(plugin.getDataFolder(), "players");
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				String identifier = file.getName().split("\\.(?=[^\\.]+$)")[0];
				if (identifier.length() > 0 && identifier.length() != 32) {
					usernames.add(identifier);
				}
			}
		}

		plugin.getLogger().info("Migrating " + usernames.size() + " players");

		Profile[] profiles = repository.findProfilesByNames(usernames.toArray(new String[usernames.size()]));

		for (Profile profile : profiles) {
			plugin.getLogger().info("Renaming " + profile.getName() + " to " + profile.getId());

			File file = new File(directory, profile.getName() + ".yml");
			File newFile = new File(directory, profile.getId() + ".yml");
			file.renameTo(newFile);

			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(newFile, true)));
				out.println("username: " + profile.getName());
				out.close();
			} catch (IOException e) {
				// Do nothing
			}
		}

		plugin.getLogger().info("Migrating sub plugins");

		for (SubPlugin subPlugin : plugin.getSubPlugins()) {
			subPlugin.migrate();
		}

		plugin.getLogger().info("Migration complete");
	}
}

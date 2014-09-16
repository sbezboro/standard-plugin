package com.sbezboro.standardplugin.tasks;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.SubPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		Map<String, String> uuidMap = new HashMap<String, String>();
		for (Profile profile : profiles) {
			uuidMap.put(profile.getName(), profile.getId());
		}

		for (String username : uuidMap.keySet()) {
			String uuid = uuidMap.get(username);

			plugin.getLogger().info("Renaming " + username + " to " + uuid);

			File file = new File(directory, username + ".yml");
			File newFile = new File(directory, uuid + ".yml");
			file.renameTo(newFile);

			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(newFile, true)));
				out.println("username: " + username);
				out.close();
			} catch (IOException e) {
				// Do nothing
			}
		}

		plugin.getLogger().info("Migrating sub plugins");

		for (SubPlugin subPlugin : plugin.getSubPlugins()) {
			subPlugin.migrate(uuidMap);
		}

		plugin.getLogger().info("Migration complete");

		plugin.reloadPlugin();
	}
}

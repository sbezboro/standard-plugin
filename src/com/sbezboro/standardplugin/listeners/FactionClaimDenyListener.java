package com.sbezboro.standardplugin.listeners;

import org.bukkit.Location;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.FactionsIntegration.ClaimDenyListenerProxy;

public class FactionClaimDenyListener implements ClaimDenyListenerProxy {
	private StandardPlugin plugin;
	
	public FactionClaimDenyListener(StandardPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean shouldDeny(Location location) {
		return plugin.getEndResetStorage().isNearPortal(location, 32);
	}

}

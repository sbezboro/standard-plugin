package com.sbezboro.standardplugin.integrations;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.P;
import com.massivecraft.factions.iface.ClaimDenyListener;
import com.sbezboro.standardplugin.StandardPlugin;

public class FactionsIntegration extends PluginIntegration {
	public static interface ClaimDenyListenerProxy {
		public boolean shouldDeny(Location location);
	}
	
	private static final String CLASS_NAME = "com.massivecraft.factions.P";
	private static final String PLUGIN_NAME = "Factions";
	private static P p;

	private static Map<ClaimDenyListenerProxy, ClaimDenyListener> listenerMap;
	
	public static void init(StandardPlugin plugin) {
		p = init(plugin, CLASS_NAME, PLUGIN_NAME);
		
		listenerMap = new HashMap<FactionsIntegration.ClaimDenyListenerProxy, ClaimDenyListener>();
	}
	
	public static boolean isWilderness(Location location) {
		return Board.getFactionAt(new FLocation(location)).isNone();
	}

	public static void addClaimDenyListener(final ClaimDenyListenerProxy proxy) {
		if (!enabled) {
			return;
		}
		
		ClaimDenyListener listener = new ClaimDenyListener() {
			
			@Override
			public boolean shouldDeny(Location location) {
				return proxy.shouldDeny(location);
			}
		};
		
		p.addClaimDenyListener(listener);
		
		listenerMap.put(proxy, listener);
	}

	public static void removeClaimDenyListener(ClaimDenyListenerProxy proxy) {
		if (!enabled) {
			return;
		}
		
		p.removeClaimDenyListener(listenerMap.get(proxy));
	}
}

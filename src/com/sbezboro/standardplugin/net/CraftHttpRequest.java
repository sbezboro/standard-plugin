package com.sbezboro.standardplugin.net;

import com.sbezboro.standardplugin.StandardPlugin;

public class CraftHttpRequest  extends ApiHttpRequest {

    public CraftHttpRequest(String uuid, String type, int amount) {
        super(StandardPlugin.getPlugin(), "log_craft", HTTPMethod.POST, null);
        addProperty("uuid", uuid);
        addProperty("type", type);
        addProperty("amount", amount);
    }

}

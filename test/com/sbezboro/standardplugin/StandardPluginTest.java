package com.sbezboro.standardplugin;

public class StandardPluginTest extends junit.framework.TestCase {

	public void testGetPlugin() throws Exception {
		assert StandardPlugin.getPlugin() != null;
	}
}

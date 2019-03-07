package me.robnoo02.plotreviewplugin.files;

import java.util.ArrayList;
import java.util.List;

import me.robnoo02.plotreviewplugin.Main;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public final class Config {

	private static final Config INSTANCE = new Config();
	private static final String MESSAGES_PATH = "messages.";
	private final Main plugin;

	private Config() {
		this.plugin = Main.getInstance();
	}

	public static Config getInstance() {
		return INSTANCE;
	}

	public String getString(String key) {
		return plugin.getConfig().getString(key);
	}

	public void setList(String path, List<String> list) {
		plugin.getConfig().set(path, list);
		plugin.saveConfig();
	}

	public void setEnumMessages() {
		for (SendMessageUtil messageVar : SendMessageUtil.values()) {
			String path = MESSAGES_PATH + messageVar.toString().toLowerCase();
			if (plugin.getConfig().contains(path)) {
				messageVar.set((ArrayList<String>) plugin.getConfig().getStringList(path));
			} else {
				ArrayList<String> none = new ArrayList<>();
				none.add("none");
				setList(path, none);
				messageVar.set(none);
			}
		}
	}
}

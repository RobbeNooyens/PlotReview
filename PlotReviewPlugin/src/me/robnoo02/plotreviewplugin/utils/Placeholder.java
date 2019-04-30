package me.robnoo02.plotreviewplugin.utils;

import me.robnoo02.plotreviewplugin.Main;

public enum Placeholder {

	SERVER_NAME("%server%", Main.getInstance().getServer().getName()), 
	PLUGIN_VERSION("%version%",Main.getInstance().getDescription().getVersion().toString()), 
	PLUGIN_DESCRIPTION("%description%",Main.getInstance().getDescription().getDescription());

	private String placeholder;
	private String placeholderContent;

	private Placeholder(String holder, String content) {
		this.placeholder = holder;
		this.placeholderContent = content;
	}

	public String value() {
		return this.placeholderContent;
	}

	public String placeholder() {
		return this.placeholder;
	}

}

package me.robnoo02.plotreview.utils;

import java.util.HashMap;

import me.robnoo02.plotreview.Query.QueryElement;
import me.robnoo02.plotreview.files.ConfigManager;

public class Placeholder {

	private final HashMap<QueryElement, String> values;
	private static HashMap<String, String> configTags = null;

	public Placeholder(HashMap<QueryElement, String> values) {
		this.values = values;
	}

	public String replace(String input) {
		for (QueryElement element : values.keySet()) {
			String placeholder = element.getPlaceHolder();
			if (input.contains(placeholder) && (values.get(element) != null)) {
				input = input.replaceAll(placeholder, values.get(element));
			}
		}
		return replaceCustom(input);
	}

	public static String replaceCustom(String input) {
		if (configTags == null) configTags = ConfigManager.getConfigTags();
		for (String s : configTags.keySet()) {
			String placeholder = "%" + s + "%";
			if (input.contains(placeholder) && configTags.get(s) != null) {
				input = input.replaceAll(placeholder, configTags.get(s));
			}
		}
		return input;
	}

}

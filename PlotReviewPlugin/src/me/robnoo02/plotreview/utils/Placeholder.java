package me.robnoo02.plotreview.utils;

import java.util.HashMap;

import me.robnoo02.plotreview.Query.QueryElement;

public class Placeholder {
	
	private final HashMap<QueryElement, String> values;
	
	public Placeholder(HashMap<QueryElement, String> values) {
		this.values = values;
	}

	public String replace(String input) {
		for(QueryElement element: values.keySet()) {
			String placeholder = element.getPlaceHolder();
			if(input.contains(placeholder) && (values.get(element) != null)) {
				input = input.replaceAll(placeholder, values.get(element));
			}
		}
		return input;
	}

}

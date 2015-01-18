package io.github.howiefh.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserAgentsRegister {
	private static Map<String, String> userAgents = new HashMap<String, String>();
	public static void register(String name,String userAgent) {
		userAgents.put(name, userAgent);
	}
	public static String getUserAgent(String name) {
		return userAgents.get(name);
	}
	public static boolean contains(String name) {
		return userAgents.containsKey(name);
	}
	public static Set<String> names(){
		return userAgents.keySet();
	}
}

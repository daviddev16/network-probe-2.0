package com.networkprobe.core.config;

import com.networkprobe.core.model.Route;
import com.networkprobe.core.model.Rule;

import java.io.Serializable;
import java.util.*;

public class NetworkConfig implements Serializable {

	private static final long serialVersionUID = 6539861461941074090L;

	public static final String DEFAULT_CONFIG_FILENAME = "network-config.yaml";

	private ServerConfig server;

	private List<Route> routes;
	private List<Rule> rules;

	public NetworkConfig(List<Route> routes, List<Rule> rules, ServerConfig server) {
		this.routes = routes;
		this.rules = rules;
		this.server = server;
	}

	public NetworkConfig() {
		this(new ArrayList<>(), new ArrayList<>(), new ServerConfig());
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public ServerConfig getServer() {
		return server;
	}

	public void setServer(ServerConfig server) {
		this.server = server;
	}

	public static final class ServerConfig {

		private int requestThreshold;

		public ServerConfig(int requestThreshold) {
			this.requestThreshold = requestThreshold;
		}

		public ServerConfig() { }

		public int getRequestThreshold() {
			return requestThreshold;
		}

		public void setRequestThreshold(int requestThreshold) {
			this.requestThreshold = requestThreshold;
		}
	}
}

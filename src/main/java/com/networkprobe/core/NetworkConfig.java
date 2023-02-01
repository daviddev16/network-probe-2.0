package com.networkprobe.core;

import com.networkprobe.core.model.Route;
import com.networkprobe.core.model.Rule;

import java.io.Serializable;
import java.util.*;

public class NetworkConfig implements Serializable {

	private static final long serialVersionUID = 6539861461941074090L;

	public static final String DEFAULT_CONFIG_FILENAME = "network-config.yaml";

	private List<Route> routes;
	private List<Rule> rules;

	public NetworkConfig(List<Route> routes, List<Rule> rules) {
		this.routes = routes;
		this.rules = rules;
	}

	public NetworkConfig() {
		this(new ArrayList<>(), new ArrayList<>());
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

}

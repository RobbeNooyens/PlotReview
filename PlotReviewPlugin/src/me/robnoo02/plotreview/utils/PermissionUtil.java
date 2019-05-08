package me.robnoo02.plotreview.utils;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public enum PermissionUtil {

	SUBMIT_PLOT("submit"), // Permission: plotreview.submit
	DEBUG("debug"), // Permission: plotreview.debug
	BYPASS_REVIEW_OWN_PLOT("review.bypass");
	
	private static final String PREFIX = "plotreview.";
	private String permission;
	
	private PermissionUtil(String perm) {
		this.permission = perm;
	}
	
	public boolean has(Player p) {
		return p.hasPermission(PREFIX + permission);
	}
	
	public boolean hasIgnoreOp(Player p) {
		return (!p.isOp()) ? has(p) : p.hasPermission(new Permission(PREFIX + permission, PermissionDefault.FALSE));
	}
	
	public boolean hasAndWarn(Player p) {
		return has(p) ? true : SendMessageUtil.NO_PERMS.send(p, false);
	}
}

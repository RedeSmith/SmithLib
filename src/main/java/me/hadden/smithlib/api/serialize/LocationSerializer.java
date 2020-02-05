package me.hadden.smithlib.api.serialize;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {
	
	public static String serializeLocation(Location loc){
		return loc.getWorld().getName() + "@" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ() + "@" + loc.getYaw() + "@" + loc.getPitch();
	}

	public static Location deserializeLocation(String s){
		String[] parts = s.split("@");
		String world = parts[0];
		double x = Double.parseDouble(parts[1]);
		double y = Double.parseDouble(parts[2]);
		double z = Double.parseDouble(parts[3]);
		float yaw = Float.parseFloat(parts[4]);
		float pitch = Float.parseFloat(parts[5]);
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
}

package me.hadden.smithlib.api.sendserver;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SendServer {

	 public static void sendServer(Player player, String serverName) {
		    ByteArrayDataOutput out = ByteStreams.newDataOutput();
		    out.writeUTF("Connect");
		    out.writeUTF(serverName);
		    player.sendPluginMessage(null, "BungeeCord", out.toByteArray());
		    //Colocar Main.getInstace() no null
		  }
	 
}

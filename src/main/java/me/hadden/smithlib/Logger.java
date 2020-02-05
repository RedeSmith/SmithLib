package me.hadden.smithlib;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Logger {

  private String prefix;
  private CommandSender sender;

  public Logger() {
    this.prefix = "[Smith:Libs] ";
    this.sender = Bukkit.getConsoleSender();
  }

  public Logger(String prefix) {
    this.prefix = prefix;
    this.sender = Bukkit.getConsoleSender();
  }

  public void info(String message) {
    this.log(SmithLevel.INFO, message);
  }

  public void warning(String message) {
    this.log(SmithLevel.WARNING, message);
  }

  public void severe(String message) {
    this.log(SmithLevel.SEVERE, message);
  }

  public void log(SmithLevel level, String message) {
    this.log(level, message, null);
  }

  public void log(SmithLevel level, String message, Throwable throwable) {
    StringBuilder result = new StringBuilder(this.prefix + message);
    if (throwable != null) {
      result.append("\n").append(throwable.getMessage());
      for (StackTraceElement ste : throwable.getStackTrace()) {
        if (ste.toString().contains("io.github.losteddev.skywars")) {
          result.append("\n").append(ste.toString());
        }
      }
    }

    this.sender.sendMessage(level.format(result.toString()));
  }

  public Logger getModule(String module) {
    return new Logger(this.prefix + "[" + module + "] ");
  }

  public static enum SmithLevel {
      INFO("§a"),
      WARNING("§c"),
      SEVERE("§4");

    private String color;

    SmithLevel(String color) {
      this.color = color;
    }

    public String format(String message) {
      return this.color + message;
    }
  }
}

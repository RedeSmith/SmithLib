package me.hadden.smithlib.api.config;

import me.hadden.smithlib.Logger;
import me.hadden.smithlib.SmithLib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

public class FileUtils {

  private static final Logger LOGGER = SmithLib.LOGGER.getModule("FileUtils");

  public static long readFile(String path) throws IOException {
    File file = new File(URLDecoder.decode(path, "UTF-8"));
    FileInputStream in = new FileInputStream(file);
    int i = -1;
    long bytes = 0;
    while ((i = in.read(new byte[1024])) != -1) {
      bytes += i;
    }
    in.close();
    return bytes;
  }

  public static void deleteFile(File file) {
    if (!file.exists()) {
      return;
    }

    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        deleteFile(f);
      }
    }

    file.delete();
  }

  public static void copyFiles(File in, File out) {
    if (in.isDirectory()) {
      if (!out.exists()) {
        out.mkdirs();
      }

      for (File file : in.listFiles()) {
        if (file.getName().equals("uid.dat")) {
          continue;
        }

        copyFiles(file, new File(out, file.getName()));
      }
    } else {
      try {
        copyFile(new FileInputStream(in), out);
      } catch (IOException ex) {
        LOGGER.log(Logger.SmithLevel.WARNING,
            "Unexpected error ocurred copying file \"" + out.getName() + "\"!", ex);
      }
    }
  }

  public static void copyFile(InputStream input, File out) {
    FileOutputStream ou = null;
    try {
      ou = new FileOutputStream(out);
      byte[] buff = new byte[1024];
      int len;
      while ((len = input.read(buff)) > 0) {
        ou.write(buff, 0, len);
      }
    } catch (IOException ex) {
      LOGGER.log(Logger.SmithLevel.WARNING, "Unexpected error ocurred copying file \"" + out.getName() + "\"!",
          ex);
    } finally {
      try {
        if (ou != null) {
          ou.close();
        }
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
      }
    }
  }
}

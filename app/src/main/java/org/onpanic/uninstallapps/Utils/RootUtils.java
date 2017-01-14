package org.onpanic.uninstallapps.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RootUtils {
    public static String getSuPath() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine();
        } catch (Throwable t) {
            return null;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public static boolean checkPermissions(String su_path) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{su_path, "-c", "ls /data/data"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}

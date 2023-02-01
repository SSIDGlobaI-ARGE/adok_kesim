package com.ardayucesan.adok_kesim.common;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PinHelper {
    public static boolean execCommand(String command) {
        boolean status = false;
        if (TextUtils.isEmpty(command)) {
            return status;
        }
        try {
            Process exec = Runtime.getRuntime().exec("su");
            InputStream response = null;
            response = exec.getInputStream();
            OutputStream outputStream = exec.getOutputStream();
            Log.e("execCommand", "before before");
            outputStream.write(command.getBytes(Charset.forName("utf-8")));
            outputStream.write("\n".getBytes());
            outputStream.write("exit\n".getBytes());
            outputStream.flush();
            Log.e("execCommand", "before wait");
            int waitFor = exec.waitFor();
            Log.e("execCommand", "execCommand command:" + command + ";waitFor=" + waitFor);
            if (waitFor == 0) {
                Log.e("Response", " " + response.toString());
                //chmod succeed
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("execCommand", "execCommand exception=" + e.getMessage());
            return false;
        }finally {
            Log.d("execCommand", "execCommand: finally im dead");
        }
        return status;
    }

    public static String exec(String command) {
        try {

            Process exec = Runtime.getRuntime().exec("su");

            OutputStream outputStream = exec.getOutputStream();
            outputStream.write(command.getBytes(Charset.forName("utf-8")));
            outputStream.write("\n".getBytes());
            outputStream.write("exit\n".getBytes());
            outputStream.flush();
            exec.waitFor();

//            Process process = Runtime.getRuntime().exec(command);

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            exec.waitFor();

            return output.toString();
        } catch (IOException e) {

            throw new RuntimeException(e);

        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }
    }
    public static void reboot() throws IOException {
        Runtime.getRuntime().exec(new String[]{"reboot"});
    }

    public static void execNotReadable(String command) {
        try {

            ProcessBuilder pb = new ProcessBuilder();
            //"/bin/bash", "-c", "su kill $pidof(cameraserver)"
            pb.command(command);
            Process p = pb.start();

//            Process process = Runtime.getRuntime().exec(command);

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().

            // Waits for the command to finish.


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

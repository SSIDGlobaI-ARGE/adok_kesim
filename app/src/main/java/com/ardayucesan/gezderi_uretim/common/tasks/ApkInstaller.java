package com.ardayucesan.gezderi_uretim.common.tasks;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.ardayucesan.gezderi_uretim.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApkInstaller extends AsyncTask<String, Integer, String> {

    private PowerManager.WakeLock mWakeLock;
    ProgressDialog progressDialog;
    int status = 0;

    private Context context;

    public void setContext(Context context, ProgressDialog progress) {
        this.context = context;
        this.progressDialog = progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Accept-Encoding", "identity");
            c.connect();
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("UPDATER", "here server not ok");
                return "Server returned HTTP " + c.getResponseCode()
                        + " " + c.getResponseMessage();
            }

            int fileLength = c.getContentLength();

            File sdcard = Environment.getExternalStorageDirectory();
            File myDir = new File(sdcard, "gezderiupdater");
            myDir.mkdirs();
            File outputFile = new File(myDir, "machbee.apk");

            if (outputFile.exists()) {
                outputFile.delete();
                Log.d("UPDATER", "sildim");
            }

            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();
            long total = 0;
            int count;

            if (!progressDialog.isShowing()) {
                is.close();
                return null;
            }

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
                Log.d("UPDATER ", "buralardayım");
            }
            while ((count = is.read(buffer)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    is.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                Log.d("UPDATER", "i know the file length");
                fos.write(buffer, 0, count);
            }

            fos.flush();
            fos.close();
            is.close();

            File update_apk = new File(myDir, "machbee.apk");
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Intent intentDelete = new Intent(Intent.ACTION_DELETE);
//                intentDelete.setData(Uri.parse("package:com.ssidglobal.machbee_gezderi"));
//                context.startActivity(intentDelete);
                Log.d("UPDATER", "sdk int > N");
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(
                        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", update_apk),
                        "application/vnd.android.package-archive");

            } else {
                Log.d("UPDATER", "sdk int < N");

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.parse("file://" + update_apk.getAbsolutePath()),
                        "application/vnd.android.package-archive");

            }
            context.startActivity(intent);


//            Intent mStartActivity = context.getPackageManager().getLaunchIntentForPackage("com.ssidglobal.machbee_gezderi");
//            int mPendingIntentId = 123456;
//            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            Log.d("UPDATER", "başladım");
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, mPendingIntent);
//            System.exit(0);

        } catch (FileNotFoundException fnfe) {
            status = 1;
            Log.e("File", "FileNotFoundException! " + fnfe);
        } catch (Exception e) {
            Log.e("UpdateAPP", "Exception " + e);
        }
        return null;
    }

//    public boolean isPackageExisted(String targetPackage) {
//        List<ApplicationInfo> packages;
//        PackageManager pm;
//
//        pm = context.getPackageManager();
//        packages = pm.getInstalledApplications(0);
//        for (ApplicationInfo packageInfo : packages) {
//            if (packageInfo.packageName.equals(targetPackage))
//                return true;
//        }
//        return false;
//    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(progress[0]);
    }

    public void onPostExecute(String result) {
        progressDialog.dismiss();
//        if(status == 1){
//
//            Toast.makeText(context,"Game Not Available", Toast.LENGTH_LONG).show();
//        }
        mWakeLock.release();
        progressDialog.dismiss();
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
    }
}

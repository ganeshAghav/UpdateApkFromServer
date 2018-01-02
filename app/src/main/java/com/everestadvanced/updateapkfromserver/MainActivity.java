package com.everestadvanced.updateapkfromserver;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    public static String package_name="com.everestadvanced.ipay";
    public static String apkName;
    public static String directroyName="/iPay/";
    private String appliction_url ="https://files.fm/down.php?i=nttcss8y&n=iPay.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadApk();
    }

    public void DownloadApk() {

        boolean isAppInstalled = appInstalledOrNot(package_name);

        if(isAppInstalled)
        {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(package_name);
            startActivity(LaunchIntent);
            new DownloadFileFromURL().execute(new String[] { package_name });
            //Toast.makeText(getApplicationContext(),"Application is already installed.",Toast.LENGTH_LONG).show();

        }
        else
        {
            new DownloadFileFromURL().execute(new String[] { package_name });
            //Toast.makeText(getApplicationContext(),"Application is not currently installed.",Toast.LENGTH_LONG).show();

        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        DownloadFileFromURL() {}

        protected String doInBackground(String... paramVarArgs) {

            Object localObject1;
            try
            {
                if (paramVarArgs[0].equals(MainActivity.this.package_name))
                {
                    MainActivity.this.apkName = "iPay";
                    URL url = new URL(appliction_url);
                    localObject1 = url.openConnection();
                    ((URLConnection)localObject1).connect();
                    int i = ((URLConnection)localObject1).getContentLength();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);
                    localObject1 = Environment.getExternalStorageDirectory();
                    Object localObject2 = new File(((File)localObject1).getAbsolutePath() + directroyName);
                    if (!((File)localObject2).exists())
                    {
                        ((File)localObject2).mkdir();
                    }
                    localObject1 = new FileOutputStream(((File)localObject1).getAbsolutePath() + directroyName + MainActivity.this.apkName + ".apk");
                    localObject2 = new byte['Ѐ'];
                    long l = 0L;
                    for (;;)
                    {
                        int j = bufferedInputStream.read((byte[])localObject2);
                        if (j == -1) {
                            break;
                        }
                        l += j;
                        publishProgress(new String[] { "" + (int)(100L * l / i) });
                        ((OutputStream)localObject1).write((byte[])localObject2, 0, j);
                    }
                    return null;
                }

            }
            catch (Throwable paramVarArg)
            {
                Log.v("Tag", "Error=>" + paramVarArg.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String paramString) {

            try
            {
                this.dialog.dismiss();
                this.dialog = null;
                File sdcard = Environment.getExternalStorageDirectory();
                paramString = sdcard.getAbsolutePath() + directroyName + MainActivity.this.apkName + ".apk";
                Intent localIntent = new Intent();
                localIntent.setAction("android.intent.action.VIEW");
                localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "application/vnd.android.package-archive");
                MainActivity.this.startActivity(localIntent);
                MainActivity.this.finish();
                return;
            }
            catch (Exception paramStrin)
            {
                for (;;) {}
            }
        }

        protected void onPreExecute() {

            this.dialog.setMessage("Downloading update...");
            this.dialog.setCancelable(false);
            this.dialog.setProgressStyle(1);
            this.dialog.show();
        }

        protected void onProgressUpdate(String... paramVarArgs) {

            this.dialog.setProgress(Integer.parseInt(paramVarArgs[0]));
        }
    }

}

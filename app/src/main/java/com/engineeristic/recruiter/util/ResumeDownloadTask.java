package com.engineeristic.recruiter.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.engineeristic.recruiter.R;

import java.io.File;
import java.io.IOException;
import java.util.List;



public class ResumeDownloadTask extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String filename;
    public ResumeDownloadTask(Context nContext, String nFileName){
        this.mContext = nContext;
        this.filename = nFileName;
    }
    private Exception exception;
    //private ProgressDialog pDialog;
    protected void onPreExecute()
    {
        super.onPreExecute();
        /*pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getResources().getString(R.string.plz_wait_lbl));
        pDialog.setCancelable(false);
        pDialog.show();*/
    }

    protected String doInBackground(String... strings)
    {

        String fileUrl = strings[0];
        String fileName = strings[1];
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory + "/Download/", mContext.getResources().getString(R.string.app_name));
        folder.mkdirs();
        File pdfFile = new File(folder, fileName);
        try
        {
            pdfFile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        FileDownloader.downloadFile(fileUrl, pdfFile);
        String result = "1";
        return result;
    }

    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        /*if ( pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }*/

        /*if ( pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }*/
        if (result!=null)
        {
            try
            {
                //showAlert(mContext.getResources().getString(R.string.download_cv_msg));
                Log.e("ResumeDownloadTak", mContext.getResources().getString(R.string.download_cv_msg));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String heading)
    {
        AlertDialog.Builder download = new AlertDialog.Builder(mContext);
        download.setTitle(heading)
                .setCancelable(true)
                .setPositiveButton(mContext.getResources().getString(R.string.view_now_lbl), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface arg, int arg1)
                            {
                                try
                                {

                                    File file = new File(Environment.getExternalStorageDirectory() + "/Download/"+mContext.getResources().getString(R.string.app_name)+"/" + filename + ".pdf");  // -> filename = maven.pdf
                                    if (file.exists())
                                    {
                                        PackageManager packageManager = mContext.getPackageManager();
                                        Intent testIntent = new Intent(Intent.ACTION_VIEW);
                                        testIntent.setType("application/pdf");
                                        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        Uri uri = Uri.fromFile(file);
                                        intent.setDataAndType(uri, "application/pdf");
                                        mContext.startActivity(intent);

                                    }
                                    else
                                    {
                                        Log.e("ResumeDownloadTak", mContext.getResources().getString(R.string.download_cv_error_msg));

                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
        AlertDialog a = download.create();
        a.show();
        a.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        a.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }
}

package com.engineeristic.recruiter.util;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by dell on 11/2/2016.
 */

public class UploadImage extends AsyncTask<String, Void, String> {

    private UploadListener mListener;
    private String jsonString = "";
    private JSONObject jsonObject = null;
    public UploadImage(UploadListener nListener){
        this.mListener = nListener;
    }

    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }
    protected String doInBackground(String... arg0) {
        return postImageInMultiPart(arg0[0], arg0[1]);
    }
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        JSONObject jsonObject = null;



        if(result.equalsIgnoreCase(Constant.MSG_NO_NETWORK)
                || result.equalsIgnoreCase(Constant.MSG_TIME_OUT) || result.equalsIgnoreCase(Constant.MSG_SERVER_ERROR)){
            // Message will display on screen
            mListener.UploadFails(result);
        }else{
            try {
                jsonObject = new JSONObject(result);
                mListener.UploadSuccess(jsonObject);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String postImageInMultiPart(String upLoadServerUri, String sourceFileUri) {
        String fileName = sourceFileUri;
        int serverResponseCode = 0;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(sourceFileUri);
        if (sourceFile.isFile()) {

            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("profile_img", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"profile_img\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                if(serverResponseCode == 200){
                    String serverResponseMessage = conn.getResponseMessage();

                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    reader.close();
                    jsonString = sb.toString();
                }else{
                    return Constant.MSG_SERVER_ERROR;
                }
            }catch(ConnectTimeoutException ex){
                return Constant.MSG_TIME_OUT;
            }catch(SocketTimeoutException  ex){
                return Constant.MSG_TIME_OUT;
            }catch(SocketException  ex){
                return Constant.MSG_NO_NETWORK;
            }catch (MalformedURLException ex) {
                return Constant.MSG_SERVER_ERROR;
            }catch(IOException ex){
                return Constant.MSG_NO_NETWORK;
            }catch (Exception e) {
                // TODO: handle exception
            }
        }
        return jsonString;
    }
}

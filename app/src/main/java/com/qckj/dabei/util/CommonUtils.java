package com.qckj.dabei.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;

import com.qckj.dabei.app.SystemConfig;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 * Created by yangzhizhong on 2019/3/22.
 */
public class CommonUtils {

    /**
     * weixin登录与支付分享
     */
    public static final String WX_APP_ID = "wxf8cc09cfbaf41350";
    public static final String WX_APP_SECRET = "c4eb1d1acb40f8fbfc55b64e00ec883c";


    /**
     * 将dip转换为px
     *
     * @param dip dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadFile(File file) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        String requestUrl = SystemConfig.getServerUrlTest("/alyFile");

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", "UTF-8");  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                String sb = PREFIX +
                        BOUNDARY +
                        LINE_END +
                        "Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END +
                        "Content-Type: application/octet-stream; charset=UTF-8" + LINE_END +
                        LINE_END;
                dos.write(sb.getBytes("UTF-8"));
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes("UTF-8"));
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes("UTF-8");
                dos.write(end_data);
                dos.flush();
                int res = conn.getResponseCode();
                InputStream input = conn.getInputStream();
                String contentEncoding = conn.getContentEncoding();
                if (contentEncoding != null && contentEncoding.contains("gzip"))
                    input = new GZIPInputStream(input);
                byte[] data = IOUtils.readInputStream(input);
                String string = new String(data, "UTF-8");
               /* StringBuilder sb1 = new StringBuilder();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }*/
                //result = sb1.toString();
                Log.e("===", "result : " + string);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}

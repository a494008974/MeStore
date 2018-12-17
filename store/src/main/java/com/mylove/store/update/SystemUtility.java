package com.mylove.store.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.store.Constanst;

public class SystemUtility {
	private static final String TAG = "SystemUtility";
	private static int sArmArchitecture = -1;
	
	public static String UPKEY = "top11688";
	public static String DOWNKEY = "down1688";

	public static String getProp(String key){
		String value = null;
		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                value = (String) m.invoke(null, key);
            } catch (Throwable e) {
            	
            }
       // }
		return value;
	}

	public static String getAllTotalMemorySize(){
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		Long size = (totalBlocks * blockSize)/(1024*1024*1024);
		String str = "";
		if (size < 4.0F) {
			str = "4.0";
		} else if (size < 8.0F) {
			str = "8.0";
		} else if (size < 16.0F) {
			str = "16.0";
		} else if (size < 32.0F) {
			str = "32.0";
		} else if (size < 64.0F) {
			str = "64.0";
		} else {
			str = "128.0";
		}
		return str + "G";
	}

	public static String getTotalMemorySize() {
		int mem =  getTotalMemory();
		if(mem > 8000)
			return "10G";
		if(mem > 5000)
			return "8G";
		if(mem > 3200)
			return "4G";
		if(mem > 1500)
			return "2G";
		if(mem > 750)
			return "1G";
		return "512M";
	}
	public static String getSystemModel() {
		return android.os.Build.MODEL;
	}
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static int getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		int initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() /1024 ;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
			Log.d("KKKK","initial_memory "+initial_memory);

		} catch (IOException e) {
		}
		return initial_memory;
	}

	public static String getValue(String type){
		String value = "";
		if ("2001".equals(type)){
			value = getSystemModel();
		}else if ("2002".equals(type)){
			value = getSystemVersion();
		}else if ("2003".equals(type)){
			value = "ARMv7 Processor 1.5GHz 八核";
		}else if ("2004".equals(type)){
			value = getTotalMemorySize();
		}else if ("2005".equals(type)){
			value = getAllTotalMemorySize();
		}else if ("2006".equals(type)){
			value = "Mali-400 MP 十六核";
		}
		return value;
	}

	public static int clearData(Context paramContext, String paramString) {
		try {
			Runtime.getRuntime().exec("pm clear " + paramString);
		} catch (Exception localException) {
		}
		return 0;
	}
	
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
		}
	}
	
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	public static String getTime(long times){
	    Date curDate = new Date(times);
		SimpleDateFormat formatime = new SimpleDateFormat ("HH:mm");
		return formatime.format(curDate);
	}
	
	public static int getArmArchitecture() {
		if (sArmArchitecture != -1)
			return sArmArchitecture;
		try {
			InputStream is = new FileInputStream("/proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				String name = "CPU architecture";
				while (true) {
					String line = br.readLine();
					String[] pair = line.split(":");
					if (pair.length != 2)
						continue;
					String key = pair[0].trim();
					String val = pair[1].trim();
					if (key.compareToIgnoreCase(name) == 0) {
						String n = val.substring(0, 1);
						sArmArchitecture = Integer.parseInt(n);
						break;
					}
				}
			} finally {
				br.close();
				ir.close();
				is.close();
				if (sArmArchitecture == -1)
					sArmArchitecture = 6;
			}
		} catch (Exception e) {
			sArmArchitecture = 6;
		}
		return sArmArchitecture;
	}

	public static int getSDKVersionCode() {
		// TODO: fix this
		return Build.VERSION.SDK_INT;
	}

	public static String getExternalStoragePath() {
		boolean exists = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (exists)
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		else
			return "/";
	}

	@SuppressWarnings("rawtypes")
	public static Object realloc(Object oldArray, int newSize) {
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType,
				newSize);
		int preserveLength = Math.min(oldSize, newSize);
		if (preserveLength > 0)
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		return newArray;
	}

	public static String getTimeString(int msec) {
		if (msec < 0) {
			return String.format("--:--:--");
		}
		int total = msec / 1000;
		int hour = total / 3600;
		total = total % 3600;
		int minute = total / 60;
		int second = total % 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	protected static String sTempPath = "/data/local/tmp";

	public static String getTempPath() {
		return sTempPath;
	}

	public static int getStringHash(String s) {
		byte[] target = s.getBytes();
		int hash = 1315423911;
		for (int i = 0; i < target.length; i++) {
			byte val = target[i];
			hash ^= ((hash << 5) + val + (hash >> 2));
		}
		hash &= 0x7fffffff;
		return hash;
	}

	public static boolean isNetworkAvailable(Context context) {  
	    ConnectivityManager connectivity = (ConnectivityManager) context  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if (connectivity == null) {  
	    	Log.e(TAG, "getSystemService rend null");  
	    } else { 
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();  
	        if (info != null) { 
	            for (int i = 0; i < info.length; i++) {  
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
	                    return true;  
	                }  
	            }  
	        }  
	    }  
	    return false;  
	}
	
    /**
     * get current data connection type name, like:Mobile/WIFI/OFFLINE
     * 
     * @param context
     * @return
     */
    public static String getConnectTypeName(Context context) {
            if (!isNetworkAvailable(context)) {
                    return "OFFLINE";
            }
            ConnectivityManager manager = (ConnectivityManager) context
                            .getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                    return info.getTypeName();
            } else {
                    return "OFFLINE";
            }
    }
    
    public static String getLocalMacAddress(Context ctx) {  
    	/*
        WifiManager wifi = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
        */
    	String text = "00:11:22:33:44:55";
		try {
			InputStream is = new FileInputStream("/sys/class/net/eth0/address");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				text = br.readLine();
					
			} finally {
				br.close();
				ir.close();
				is.close();
			}
		} catch (Exception e) {
            Log.e(TAG, "Open File Error!" + e.toString());  
		}
        
        return text.trim();
    }
    
    public static String getCharAndNumr(int length)     
	{     
	    String val = "";
	    Random random = new Random();     
	    for(int i = 0; i < length; i++)     
	    {     
	        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字        
	        if("char".equalsIgnoreCase(charOrNum)) // 字符串     
	        {     
	            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
	            val += (char) (choice + random.nextInt(26));     
	        }     
	        else if("num".equalsIgnoreCase(charOrNum)) // 数字     
	        {     
	            val += String.valueOf(random.nextInt(10));     
	        }     
	    }     
	    return val;     
	} 
    
    public static String getLocalIpAddress() {  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface  
                    .getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf  
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress()) {  
                        return inetAddress.getHostAddress().toString();  
                    }  
                }  
            }  
        } catch (SocketException ex) {

        }
        return null;  
    }
    
    public static String getCurrentVersion(Context mCtx) {
	    PackageManager packageManager = mCtx.getPackageManager();  
	    PackageInfo packInfo;
	    String ver = "1.0";
		try {
			packInfo = packageManager.getPackageInfo(mCtx.getPackageName(), 0);
			ver = packInfo.versionName;
		} catch (Exception e) {
		}
	    return ver;   
	}
    
    public static String getSn(Context mCtx){
    	String androidId = Settings.Secure.getString(mCtx.getContentResolver(),Settings.Secure.ANDROID_ID);
    	String sn = "0123456789";
    	try {
    		sn = androidId.substring(androidId.length()-10, androidId.length());
    		sn = sn.replaceAll("a", "0");
    		sn = sn.replaceAll("b", "1");
    		sn = sn.replaceAll("c", "2");
    		sn = sn.replaceAll("d", "3");
    		sn = sn.replaceAll("e", "4");
    		sn = sn.replaceAll("f", "5");
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return sn;
    }
    
    public static int getCurrentVersionCode(Context mCtx) {

		PackageManager packageManager = mCtx.getPackageManager();
		PackageInfo packInfo;
		int verCode = 0;
		try {
			packInfo = packageManager.getPackageInfo(mCtx.getPackageName(), 0);
			verCode = packInfo.versionCode;
		} catch (Exception e) {

		}

		return verCode;
	}
    
    public static String getParam(Context mCtx){
		String str = "";
		try{
			Build bd = new Build();
			JSONObject jo = new JSONObject();
			jo.put("model", Build.MODEL);
			jo.put("mac", getLocalMacAddress(mCtx));
			jo.put("pkg", getPackageName(mCtx));
			jo.put("ver", getCurrentVersion(mCtx));
			jo.put("code", getCurrentVersionCode(mCtx));
			jo.put("channel", Constanst.getMeta(BaseApplication.getAppContext(),Constanst.UMENG_CHANNEL));
			String mm = DesHelper.encrypt(jo.toString(), UPKEY);
			str = URLEncoder.encode(mm, "utf8");
		}catch(Exception e){
			str = "";
		}
		return str;
	}


    public static String getPackageName(Context mCtx) {
		PackageManager packageManager = mCtx.getPackageManager();
		PackageInfo packInfo;
		String name = "";
		try {
			packInfo = packageManager.getPackageInfo(mCtx.getPackageName(), 0);
			name = packInfo.packageName;
		} catch (Exception e) {

		}
		return name;
	}

}

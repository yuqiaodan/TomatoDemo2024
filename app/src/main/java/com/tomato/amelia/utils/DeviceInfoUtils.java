package com.tomato.amelia.utils;

/**
 * author: created by tomato on 2024/2/4 14:46
 * description:设备信息工具 java代码
 */

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
public class DeviceInfoUtils {



    /**
     * 判断设备是否启用adb
     * */
    public static boolean enableAdb(Context context){
        int enable = Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        return enable > 0;
    }

    /**
     * 判断设备是否开启root权限
     * */
    public static boolean isRoot(){
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath))
            return true;
        return new File(xBinPath).exists() && isExecutable(xBinPath);
    }

    /**
     * 判断设备是否插入sim卡
     * */
    public static boolean hasSimCard(Context context){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        boolean result = true;
        if (simState == TelephonyManager.SIM_STATE_ABSENT){
            result = false;
        }else if (simState == TelephonyManager.SIM_STATE_UNKNOWN){
            result = false;
        }
        return result;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

    /**
     * 判断设备 是否使用代理上网
     * */
    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            //设备大于android4.0
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            //设备小于android4.0
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }


    /**
     * 判断设备 是否使用VPN代理上网
     * */
    public static boolean isVpnUsed(Context context) {
        boolean isVpn = false;
        //检查网络是否链接
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //判断时候有网络
        if (networkInfo != null) {
            try {
                Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
                if (niList != null) {
                    for (NetworkInterface intf : Collections.list(niList)) {
                        if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                            continue;
                        }
                        if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                            isVpn = true;
                        }
                    }
                    if (!isVpn) {
                        networkInfo.getTypeName();

                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return isVpn;
    }


    /**
     * 判断设备是否是模拟器
     */
    public static boolean isEmulator(Context context) {
        Log.i("MyInfoManager", "notHasLightSensorManager " + notHasLightSensorManager(context));
        Log.i("MyInfoManager", "ifFeatures " + ifFeatures(context));
        Log.i("MyInfoManager", "checkIsNotRealPhone " + checkIsNotRealPhone());
        return notHasLightSensorManager(context) || ifFeatures(context) || checkIsNotRealPhone();
    }

    /**
     * 判断蓝牙是否有效
     *
     * @return true 判定为模拟器
     *
     * 需要蓝牙权限 暂不调用
     */
    /*private static boolean notHasBlueTooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return true;
        } else {
            // 如果蓝牙不一定有效的。获取蓝牙名称，若为 null 则默认为模拟器
            String name = bluetoothAdapter.getName();
            if (TextUtils.isEmpty(name)) {
                return true;
            } else {
                return false;
            }
        }
    }*/

    /**
     * 依据是否存在 光传感器 来判断是否为模拟器
     *
     * @param context
     * @return true 判定为模拟器
     */
    private static boolean notHasLightSensorManager(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensor == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据部分特征参数设备信息来判断是否为模拟器
     *
     * @return
     */
    private static boolean ifFeatures(Context context) {

        Log.i("MyInfoManager", "ifFeatures各参数 "+
                "\nBuild.FINGERPRINT:" + Build.FINGERPRINT+
                "\nBuild.MODEL:" + Build.MODEL+
                "\nBuild.SERIAL:" + Build.SERIAL+
                "\nBuild.MANUFACTURER:" + Build.MANUFACTURER+
                "\nBuild.BRAND:" + Build.BRAND+
                "\nBuild.DEVICE:" + Build.DEVICE+
                "\nBuild.PRODUCT:" + Build.PRODUCT);

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getNetworkOperatorName().toLowerCase().equals("android");
    }

    /*
     *根据CPU是否为电脑来判断是否为模拟器
     *返回:true 为模拟器
     */
    private static boolean checkIsNotRealPhone() {
        String cpuInfo = readCpuInfo();
        if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) {
            return true;
        }
        return false;
    }

    /**
     * 根据 CPU 是否为电脑来判断是否为模拟器
     *
     * @return
     */
    private static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            Process process = processBuilder.start();
            StringBuffer stringBuffer = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                stringBuffer.append(readLine);
            }
            responseReader.close();
            result = stringBuffer.toString().toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

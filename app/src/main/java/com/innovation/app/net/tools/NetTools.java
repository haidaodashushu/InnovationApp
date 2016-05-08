package com.innovation.app.net.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author WangZhengkui on 2015-06-17 11:13
 */
public class NetTools {
    public static String encodeUrl(String url,Map<String,String> params){
        String mParams = encodeParameters(params,"UTF-8");
        if (!mParams.equals("")) {
            if (url.endsWith("?")) {
                url += mParams;
            } else {
                url = url + "?" + mParams;
            }
        }
        return url;
    }
    public static String replace(String url,String name,String value){
        try {
            return url.replace(name,value);
        }catch (Exception e){
            return url;
        }
    }

    private static String encodeParameters(Map<String, String> params, String paramsEncoding) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }


            return StringsUtils.removeParentheses(encodedParams.toString(), ' ', '&');
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }



    /** 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 */
    public static final int NETWORKTYPE_WAP = 1;
    /** 2G网络 */
    public static final int NETWORKTYPE_2G = 2;

    /** 3G*/
    public static final int NETWORKTYPE_3G = 3;

    /** 4G*/
    public static final int NETWORKTYPE_4G = 4;

    /** wifi网络 */
    public static final int NETWORKTYPE_WIFI = 5;


    public static String getNetWorkType(Context context) {
        int nNetWorkType = getNetworkTypeNum(context);
        switch (nNetWorkType) {
            case NETWORKTYPE_WAP:
            case NETWORKTYPE_2G:
                return "2G";
            case NETWORKTYPE_3G:
                return "3G";
            case NETWORKTYPE_4G:
                return "4G";
            case NETWORKTYPE_WIFI:
                return "wifi";
            default:return "4G";
        }
    }
    public static int getNetworkTypeNum(Context context) {
        int strNetworkType = 0;

        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = NETWORKTYPE_WIFI;
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

//                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
//                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    case 11:
                        strNetworkType = NETWORKTYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
//                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 12
                    case 12:
//                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 14
                    case 14:
//                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    case 15:
                        strNetworkType = NETWORKTYPE_3G;
                        break;
//                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    case 13:
                        strNetworkType = NETWORKTYPE_4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = NETWORKTYPE_3G;
                        }
                        else
                        {
                            strNetworkType = NETWORKTYPE_4G;
                        }

                        break;
                }

                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.e("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }
}

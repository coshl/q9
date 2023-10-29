package com.summer.web.util;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class GoogleAuthUtil {
    private static String googleChartUrl = "https://chart.googleapis.com/chart";//ConfigProperties.getInstance()
//            .getValue("google.chart.url", "https://chart.googleapis.com/chart");


    /**
     * Generate a random secret key. This must be saved by the server and
     * associated with the users account to verify the code displayed by Google
     * Authenticator. The user must register this secret on their device.
     * 生成一个随机秘钥
     *
     * @return secret key
     */
    public static String generateSecretKey() {
        GoogleAuthenticator gAuth  = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth .createCredentials();
        String keyStr = key.getKey();
        return keyStr;
    }


    public static String getQcode(String email,String secret) {

        String url = googleChartUrl
                + "?chs=200x200&chld=M|0&cht=qr&chl=otpauth://totp/"+ email + "%3Fsecret%3D" + secret;

        return url;

    }

    public static boolean check_code(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(secret, code);
        return isCodeValid;
    }


}
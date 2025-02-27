package hnqd.aparmentmanager.paymentservice.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
@Getter
@Lazy
//@ConfigurationProperties(prefix = "vnpay")
public class VNPayConfig {

//    private static volatile VNPayConfig instance; // volatile đảm bảo đọc và ghi luôn nhất quán giữa các threads,
                                                  // tránh tình trạng đọc thấy object chưa khởi tạo xong
    @Value("${vnpay.vnp_payurl}")
    private String vnp_payurl;
    @Value("${vnpay.vnp_returnurl}")
    private String vnp_returnurl;
    @Value("${vnpay.vnp_tmnCode}")
    private String vnp_tmnCode;
    @Value("${vnpay.vnp_hashsecret}")
    private String vnp_hashsecret;
//    @Value("${vnpay.vnp_apiurl}")
//    private String vnp_apiurl;

//    private VNPayConfig() {
//        vnp_PayUrl = System.getenv("VNPay.vnp_PayUrl");
//        vnp_Returnurl = System.getenv("VNPay.vnp_Returnurl");
//        vnp_TmnCode = System.getenv("VNPay.vnp_TmnCode");
//        vnp_HashSecret = System.getenv("VNPay.vnp_HashSecret");
//        vnp_ApiUrl = System.getenv("VNPay.vnp_ApiUrl");
//    }

//    public static VNPayConfig getInstance() {
//        if (instance == null) {
//            synchronized (VNPayConfig.class) { // synchronized sẽ lock đối tượng lại,
//                                               // chặn nhiều threads cùng tạo instance
//                if (instance == null) {        // check lại lần nữa khi vào vùng synchronized
//                    instance = new VNPayConfig();
//                }
//            }
//        }
//        return instance;
//    }

    public static String md5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    //Util for VNPAY
    public String hashAllFields(Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(vnp_hashsecret, sb.toString());
    }

    public static String hmacSHA512(String key, String data) {
        Mac sha512Hmac;
        String result;
        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.US_ASCII);
            sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(data.getBytes(StandardCharsets.US_ASCII));
            result = Hex.encodeHexString(macData);
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        } finally {
            // Put any cleanup here
            System.out.println("Done");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getLocalAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

}

package cn.jeelearn.house.common.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

/**
 * @Description: hash加密工具类
 * @Auther: lyd
 * @Date: 2018/12/12
 * @Version:v1.0
 */
public class HashUtils {

    private static final HashFunction FUNCTION = Hashing.md5();

    private static final String SALT = "mooc.com";

    public static String encryPassword(String password){
        HashCode hashCode = FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
        return hashCode.toString();
    }
}


package util;

import java.nio.charset.Charset;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class EncryptionUtil {

    public static String md5(String value){
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(value, Charset.forName("UTF-8"));
        return hasher.hash().toString();
    }

    public static String sha1(String value){
        Hasher hasher = Hashing.sha1().newHasher();
        hasher.putString(value,Charset.forName("UTF-8"));
        return hasher.hash().toString();
    }

    public static String sha256(String value){
        Hasher hasher = Hashing.sha256().newHasher();
        hasher.putString(value,Charset.forName("UTF-8"));
        return hasher.hash().toString();
    }

    public static String base64Encode(String value){
        return Base64.encodeBase64String(value.getBytes());
    }

    public static String base64Decode(String value){
        return new String(Base64.decodeBase64(value));
    }
}

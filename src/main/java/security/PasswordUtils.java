package security;

import hibernate.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

//Utils to protect passwords
public class PasswordUtils {

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String getHashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public static boolean checkPassword(User user, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(user.getSalt());

        byte[] hashedInputPassword = md.digest(password.getBytes());
        String newHash = Base64.getEncoder().encodeToString(hashedInputPassword);

        return newHash.equals(user.getPassword());
    }
}

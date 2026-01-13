package com.infyniteloop.isec.security.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtKeyLoader {

    public static PrivateKey loadPrivateKey() {
        try {
            String base64Key = System.getenv("JWT_PRIVATE_KEY");

            if (base64Key == null || base64Key.isBlank()) {
                throw new IllegalStateException("JWT_PRIVATE_KEY env variable not set");
            }

            byte[] decodedKey = Base64.getDecoder().decode(base64Key);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key from env", e);
        }
    }

    public static PublicKey loadPublicKey() {
        try {
            String base64Key = System.getenv("JWT_PUBLIC_KEY");
            if (base64Key == null || base64Key.isBlank()) {
                throw new IllegalStateException("JWT_PUBLIC_KEY env var not set");
            }

            byte[] decoded = Base64.getDecoder().decode(base64Key);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(spec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key from env", e);
        }
    }
}

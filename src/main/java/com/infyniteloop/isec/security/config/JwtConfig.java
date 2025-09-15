package com.infyniteloop.isec.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${spring.app.jwt.privateKeyPath}")
    private String privateKeyPath;

    @Value("${spring.app.jwt.publicKeyPath}")
    private String publicKeyPath;

    /**
     * Load RSA PrivateKey from PEM file
     *
     * Generate a new RSA private key in PKCS#8 format using the following command
     * openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
     *
     * @return PrivateKey object
     * @throws Exception if any error occurs
     */
    @Bean
    public PrivateKey loadPrivateKey() throws Exception {
        // 1. Read file content
        String keyContent = new String(Files.readAllBytes(Paths.get(privateKeyPath)));

        // 2. Remove PEM headers and whitespace
        keyContent = keyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        // 3. Base64 decode
        byte[] decodedKey = Base64.getDecoder().decode(keyContent);

        // 4. Generate PrivateKey object
        //RSA private keys are usually encoded in PKCS#8 format.
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Load RSA PublicKey from PEM file
     *
     * Generate a new RSA public key in X.509 format using the following command  from the private key
     * openssl rsa -pubout -in private_key.pem -out public_key.pem
     *
     * @return PublicKey object
     * @throws Exception if any error occurs
     */
    @Bean
    public PublicKey loadPublicKey() throws Exception {
        // Load the public key PEM file from resources or disk
        String keyContent = new String(Files.readAllBytes(Paths.get(publicKeyPath)));

        // Remove PEM headers/footers
        keyContent = keyContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", ""); // remove newlines

        byte[] decodedKey = Base64.getDecoder().decode(keyContent);

        //RSA public keys are usually encoded in X.509 format.
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }



}
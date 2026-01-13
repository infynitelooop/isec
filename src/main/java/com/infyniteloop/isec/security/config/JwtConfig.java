package com.infyniteloop.isec.security.config;

import com.infyniteloop.isec.security.util.JwtKeyLoader;
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

    @Bean(name = "loadPrivateKey")
    public PrivateKey jwtPrivateKey() {
        return JwtKeyLoader.loadPrivateKey();
    }

    @Bean(name = "loadPublicKey")
    public PublicKey jwtPublicKey() {
        return JwtKeyLoader.loadPublicKey();
    }
}
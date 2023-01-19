package org.sid.securityservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@ConfigurationProperties(prefix = "rsa")
public record RsaKeysConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
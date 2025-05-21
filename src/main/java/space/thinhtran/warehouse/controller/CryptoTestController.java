package space.thinhtran.warehouse.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import space.thinhtran.warehouse.annotation.PublicEndpoint;
import space.thinhtran.warehouse.util.CryptoUtil;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
public class CryptoTestController {

    private SecretKey aesKey;
    private byte[] iv;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    // Thông tin file PKCS12
    private static final String P12_PATH = "src/main/resources/keystore.p12";
    private static final String P12_PASSWORD = "123456";
    private static final String P12_ALIAS = "mykey";

    @PostConstruct
    public void init() throws Exception {
        aesKey = CryptoUtil.generateAESKey(128);
        iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        privateKey = CryptoUtil.getPrivateKeyFromPKCS12(P12_PATH, P12_PASSWORD, P12_ALIAS);
        publicKey = CryptoUtil.getPublicKeyFromPKCS12(P12_PATH, P12_PASSWORD, P12_ALIAS);
    }

    @PostMapping("/aes/encrypt")
    @PublicEndpoint
    public Map<String, String> encryptAES(@RequestBody Map<String, String> request) throws Exception {
        String plaintext = request.get("text");
        String encrypted = CryptoUtil.encryptAES(plaintext, aesKey, iv);
        Map<String, String> response = new HashMap<>();
        response.put("cipherText", encrypted);
        response.put("ivBase64", Base64.getEncoder().encodeToString(iv));
        return response;
    }

    @PublicEndpoint
    @PostMapping("/aes/decrypt")
    public Map<String, String> decryptAES(@RequestBody Map<String, String> request) throws Exception {
        String cipherText = request.get("cipherText");
        String decrypted = CryptoUtil.decryptAES(cipherText, aesKey, iv);
        Map<String, String> response = new HashMap<>();
        response.put("plainText", decrypted);
        return response;
    }

    ///
    @PublicEndpoint
    @PostMapping("/rsa/sign")
    public Map<String, String> signData(@RequestBody Map<String, String> request) throws Exception {
        String data = request.get("data");
        String signature = CryptoUtil.signSHA256withRSA(data, privateKey);
        Map<String, String> response = new HashMap<>();
        response.put("signature", signature);
        response.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        return response;
    }

    @PublicEndpoint
    @PostMapping("/rsa/verify")
    public Map<String, Object> verifySignature(@RequestBody Map<String, String> request) throws Exception {
        String data = request.get("data");
        String signature = request.get("signature");
        boolean valid = CryptoUtil.verifySignature(data, signature, publicKey);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", valid);
        return response;
    }
}

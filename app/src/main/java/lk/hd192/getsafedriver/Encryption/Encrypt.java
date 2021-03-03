package lk.hd192.getsafedriver.Encryption;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Encrypt {
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Cipher cipher;
    String encryptedMsg,decryptedMsg;


    //default  generate public and private keys
    public void generateRSAKey() throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(2048);
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
//        Log.e("key",keyGen.generateKeyPair().getPrivate().toString());
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String encryptData(PublicKey key, String message) throws  GeneralSecurityException {
        this.cipher = Cipher.getInstance("RSA");

        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        encryptedMsg=  this.cipher.doFinal(message.getBytes()).toString();
        return encryptedMsg;
    }

    public byte[] decryptData(PrivateKey privateKey, String message) throws GeneralSecurityException {
        this.cipher = Cipher.getInstance("RSA");
        this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return this.cipher.doFinal(message.getBytes());
    }




}

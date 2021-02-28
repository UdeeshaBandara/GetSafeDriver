package lk.hd192.getsafedriver.Encryption;

import android.util.Log;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Encrypt {
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;


    //default  generate public and private keys
    public Encrypt(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA","hi");
        this.keyGen.initialize(keylength);
        Log.e("key",keyGen.generateKeyPair().getPrivate().toString());
    }

}

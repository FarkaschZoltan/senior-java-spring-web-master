package hu.ponte.hr.services;

import hu.ponte.hr.exceptions.SignException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class SignService {

  private final File privateKeyFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\config\\keys\\key.private");
  private final File publicKeyFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\config\\keys\\key.pub");

  public String signImage(byte[] imageBytes) throws SignException {
    try {
      byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyFile.getAbsolutePath()));

      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      Signature signature = Signature.getInstance("SHA256withRSA");

      signature.initSign(keyFactory.generatePrivate(keySpec));
      signature.update(imageBytes);

      return Base64.getEncoder().encodeToString(signature.sign());
    } catch (Exception e){
      throw new SignException("Error while trying to sign image", e);
    }
  }

  public boolean verifySignature(byte[] imageBytes, String digitalSign) throws SignException{
    try {
      byte[] keyBytes = Files.readAllBytes(Paths.get(publicKeyFile.getAbsolutePath()));

      KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      Signature signature = Signature.getInstance("SHA256withRSA");

      signature.initVerify(keyFactory.generatePublic(keySpec));
      signature.update(imageBytes);

      return signature.verify(Base64.getDecoder().decode(digitalSign));
    } catch (Exception e){
      throw new SignException("Error while trying to verify signature", e);
    }
  }

}

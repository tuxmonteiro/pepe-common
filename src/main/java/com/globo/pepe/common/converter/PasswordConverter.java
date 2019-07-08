/*
 * Copyright (c) 2017-2019 Globo.com
 * All rights reserved.
 *
 * This source is subject to the Apache License, Version 2.0.
 * Please see the LICENSE file for more information.
 *
 * Authors: See AUTHORS file
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globo.pepe.common.converter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

    private static final char[] KEY = Optional.ofNullable(System.getenv("PEPE_SECRET")).orElse("mysecret").toCharArray();

    @Override
    public String convertToDatabaseColumn(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            SecretKey secretKey = getSecretKey(salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8.name());
            byte[] encryptedText = getBytes(null, passwordBytes, secretKey, Cipher.ENCRYPT_MODE, cipher);

            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

            // concatenate salt + iv + ciphertext
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(salt);
            outputStream.write(iv);
            outputStream.write(Objects.requireNonNull(encryptedText));

            // properly encode the complete ciphertext
            return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            byte[] ciphertext = DatatypeConverter.parseBase64Binary(dbData);
            if (ciphertext.length < 48) {
                return null;
            }
            byte[] salt = Arrays.copyOfRange(ciphertext, 0, 16);
            byte[] iv = Arrays.copyOfRange(ciphertext, 16, 32);
            byte[] ct = Arrays.copyOfRange(ciphertext, 32, ciphertext.length);

            SecretKey secretKey = getSecretKey(salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] plaintext = getBytes(iv, ct, secretKey, Cipher.DECRYPT_MODE, cipher);

            return new String(Objects.requireNonNull(plaintext), StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getBytes(byte[] iv, byte[] ct, SecretKey secretKey, int mode, Cipher cipher) throws GeneralSecurityException {
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        if (Cipher.DECRYPT_MODE == mode) {
            cipher.init(mode, secret, new IvParameterSpec(iv));
            return cipher.doFinal(ct);
        }
        if (Cipher.ENCRYPT_MODE == mode) {
            cipher.init(mode, secret);
            return cipher.doFinal(ct);
        }
        return null;
    }

    private SecretKey getSecretKey(byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(KEY, salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec);
    }
}

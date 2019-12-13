package com.codevsolution.base.encrypt;

import android.content.Context;
import android.util.Base64;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.settings.Preferencias;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ENCODE;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ENCODEPASS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;

public class EncryptUtil {

    static Context context = AppActivity.getAppContext();
    static String idUser = AndroidUtil.getSharePreference(context, USERID, USERID, NULL);

    public static final String AES = "AES";
    public static final String SHA256 = "SHA-256";
    public static final String UTF8 = "UTF-8";

    public static String desencriptarStrAES(String datos, String password) throws Exception {

        SecretKeySpec secretKey = generateKeyAES(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
        return new String(datosDesencriptadosByte);


    }

    public static String desencriptarPassAES(String datos, String password) throws Exception {

        SecretKeySpec secretKey = generateKeyAES(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
        return new String(datosDesencriptadosByte);

    }

    public static String encriptarStrAES(String datos, String password) throws Exception {
        SecretKeySpec secretKey = generateKeyAES(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        return Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
    }

    public static byte[] desencriptarByteAES(String datos, String password) throws Exception {
        SecretKeySpec secretKey = generateKeyAES(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        return cipher.doFinal(datosDescoficados);
    }

    public static byte[] encriptarByteAES(String datos, String password) throws Exception {
        SecretKeySpec secretKey = generateKeyAES(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(datos.getBytes());
    }

    public static String desencriptarStr(String datos, String password, String algoritmo, String algoritmoKey) throws Exception {
        SecretKeySpec secretKey = generateKey(password, algoritmo, algoritmoKey);
        Cipher cipher = Cipher.getInstance(algoritmoKey);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
        return new String(datosDesencriptadosByte);
    }

    public static String encriptarStr(String datos, String password, String algoritmo, String algoritmoKey) throws Exception {
        SecretKeySpec secretKey = generateKey(password, algoritmo, algoritmoKey);
        Cipher cipher = Cipher.getInstance(algoritmo);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        return Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
    }

    public static byte[] desencriptarByte(String datos, String password, String algoritmo, String algoritmoKey) throws Exception {
        SecretKeySpec secretKey = generateKey(password, algoritmo, algoritmoKey);
        Cipher cipher = Cipher.getInstance(algoritmo);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        return cipher.doFinal(datosDescoficados);
    }

    public static byte[] encriptarByte(String datos, String password, String algoritmo, String algoritmoKey) throws Exception {
        SecretKeySpec secretKey = generateKey(password, algoritmo, algoritmoKey);
        Cipher cipher = Cipher.getInstance(algoritmo);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(datos.getBytes());
    }

    public static String byteToEncodeStr(byte[] bytes) {

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static byte[] encodeStrToDecodeBytes(String encodeString) {

        return Base64.decode(encodeString, Base64.DEFAULT);
    }

    public static SecretKeySpec generateKeyAES(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(SHA256);
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        key = sha.digest(key);
        return new SecretKeySpec(key, AES);
    }

    public static SecretKeySpec generateKey(String password, String algoritmo, String algoritmoKey) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(algoritmo);
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        key = sha.digest(key);
        return new SecretKeySpec(key, algoritmoKey);
    }

    public static String generaPass() {

        return UUID.randomUUID().toString();
    }

    public static Object decodificaStrObj(final Class<?> clase, Object dataSnapshot) {

        if (clase == String.class) {
            String codeStr = null;
            String valor = (String) dataSnapshot;
            char clave = '-';

            if (InteractorBase.encrypt && valor.substring(0, 10).equals(ENCODEPASS)) {

                try {
                    codeStr = desencriptarStrAES(valor.substring(10), InteractorBase.key);
                    return (codeStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (InteractorBase.encrypt && valor.substring(0, 6).equals(ENCODE)) {

                try {
                    codeStr = desencriptarStrAES(valor.substring(6), idUser);
                    return (codeStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (InteractorBase.encrypt && valor.length() > 36
                    && valor.charAt(8) == (int) clave && valor.charAt(13) == (int) clave
                    && valor.charAt(18) == (int) clave && valor.charAt(23) == (int) clave) {
                try {
                    codeStr = EncryptUtil.desencriptarStrAES(valor.substring(36),
                            valor.substring(0, 36));
                    return (codeStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return (valor);
            }

        }
        return (dataSnapshot);
    }

    public static String decodificaStr(String datoCode) {

        String codeStr = null;
        char clave = '-';
        if (datoCode != null && datoCode.length() > 6) {

            if (datoCode.length() > 10 && datoCode.substring(0, 10).equals(ENCODEPASS)) {

                try {
                    if (InteractorBase.key != null && verificarPass(InteractorBase.key)) {
                        if (comprobarIsCode(datoCode)) {
                            codeStr = desencriptarStrAES(datoCode.substring(10), InteractorBase.key);
                            return codeStr;
                        }
                    }
                    return datoCode;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (datoCode.substring(0, 6).equals(ENCODE)) {

                try {
                    codeStr = desencriptarStrAES(datoCode.substring(6), idUser);
                    return (codeStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (datoCode.length() > 36
                    && datoCode.charAt(8) == (int) clave && datoCode.charAt(13) == (int) clave
                    && datoCode.charAt(18) == (int) clave && datoCode.charAt(23) == (int) clave) {
                try {
                    codeStr = EncryptUtil.desencriptarStrAES(datoCode.substring(36),
                            datoCode.substring(0, 36));
                    return (codeStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return (datoCode);

    }

    public static boolean verificarPass(String passInput) {

        try {
            String pass = AndroidUtil.getSharePreference(context, PREFERENCIAS + idUser, ENCODEPASS, NULL);
            String codeStr = desencriptarPassAES(pass, passInput);
            return passInput.equals(codeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }


    public static boolean comprobarIsCode(String datoCode) {

        char clave = '-';

        return datoCode != null && ((datoCode.length() > 10 && datoCode.substring(0, 10).equals(ENCODEPASS)) ||
                (datoCode.length() > 6 && datoCode.substring(0, 6).equals(ENCODE)) || (datoCode.length() > 36
                && datoCode.charAt(8) == (int) clave && datoCode.charAt(13) == (int) clave
                && datoCode.charAt(18) == (int) clave && datoCode.charAt(23) == (int) clave));
    }

    public static boolean comprobarIsCodeEncodePass(String datoCode) {

        return datoCode != null && datoCode.length() > 10 && datoCode.substring(0, 10).equals(ENCODEPASS);
    }

    public static boolean comprobarIsCodeEncode(String datoCode) {

        return datoCode != null && datoCode.length() > 6 && datoCode.substring(0, 6).equals(ENCODE);
    }

    public static boolean comprobarIsCodeGen(String datoCode) {

        char clave = '-';

        return datoCode != null && (datoCode.length() > 36
                && datoCode.charAt(8) == (int) clave && datoCode.charAt(13) == (int) clave
                && datoCode.charAt(18) == (int) clave && datoCode.charAt(23) == (int) clave);
    }

    public static String codificarStrTrans(String valor) {

        return generaPass() + valor;
    }

    public static String codificaStr(String valor) {

        if (valor != null && InteractorBase.encrypt && !comprobarIsCode(valor)) {

            String codeSrt = valor;

            if (AndroidUtil.getSharePreference(context, PREFERENCIAS + idUser, Preferencias.CIFRADO, false) &&
                    AndroidUtil.getSharePreference(context, PREFERENCIAS + idUser, Preferencias.CIFRADOPASS, false)) {

                try {
                    if (InteractorBase.key != null && verificarPass(InteractorBase.key)) {
                        codeSrt = EncryptUtil.encriptarStrAES(valor, InteractorBase.key);
                        return (ENCODEPASS + codeSrt);
                    }

                    return codeSrt;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (AndroidUtil.getSharePreference(context, PREFERENCIAS + idUser, Preferencias.CIFRADO, false)) {

                try {

                    codeSrt = EncryptUtil.encriptarStrAES(valor, idUser);
                    return (ENCODE + codeSrt);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

        return valor;

    }

    public static void codificaPass() {

        String codeSrt = null;

        try {
            codeSrt = EncryptUtil.encriptarStrAES(InteractorBase.key, InteractorBase.key);
            AndroidUtil.setSharePreference(context, PREFERENCIAS + idUser, ENCODEPASS, codeSrt);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cifrarBase(ArrayList<String[]> camposTablas) {

        for (String[] camposTabla : camposTablas) {
            ListaModeloSQL listaModeloSQL = new ListaModeloSQL(camposTabla, true);
            for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
                //if (modeloSQL.getString(CAMPO_TIMESTAMP)!=null && modeloSQL.getString(CAMPO_CREATEREG)!=null &&
                //!modeloSQL.getString(CAMPO_TIMESTAMP).equals(modeloSQL.getString(CAMPO_CREATEREG))) {
                for (int i = 2; i < Integer.parseInt(camposTabla[0]); i += 3) {
                    String dato = modeloSQL.getString(camposTabla[i]);
                    if (dato != null) {

                        if (comprobarIsCodeEncodePass(dato)) {

                        } else if (!comprobarIsCode(dato)) {
                            int res = CRUDutil.actualizarCampo(modeloSQL, camposTabla[i], dato);
                            if (res > 0) {
                            }
                        } else if (comprobarIsCodeEncode(dato)) {
                            dato = decodificaStr(dato);
                            int res = CRUDutil.actualizarCampo(modeloSQL, camposTabla[i], dato);
                            if (res > 0) {
                            }
                        }
                    }
                }
                //}
            }

        }

    }

    public static void desCifrarBase(ArrayList<String[]> camposTablas) {

        for (String[] camposTabla : camposTablas) {
            ListaModeloSQL listaModeloSQL = new ListaModeloSQL(camposTabla);
            for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
                for (int i = 5; i < camposTabla.length; i += 3) {
                    String dato = modeloSQL.getString(camposTabla[i]);
                    if (comprobarIsCode(dato)) {
                        CRUDutil.actualizarCampo(modeloSQL, camposTabla[i], decodificaStr(dato), false);
                    }
                }
            }
        }

    }
}

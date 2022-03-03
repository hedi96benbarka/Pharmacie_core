package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author ERRAYHAN
 */
public class Helper {

    public static String IncrmenterString7(String s) {
        Integer d = 0;
        d = Integer.parseInt(s);
        d++;
        String new_Code = d.toString();
        for (int i = 0; i < 7 - d.toString().length(); i++) {
            new_Code = "0" + new_Code;
        }

        return new_Code;
    }

    public static String IncrmenterString5(String s) {
        Integer d = 0;
        d = Integer.parseInt(s);
        d++;
        String new_Code = d.toString();
        for (int i = 0; i < 5 - d.toString().length(); i++) {
            new_Code = "0" + new_Code;
        }

        return new_Code;
    }

    public static String IncrementString(String s, int length) {
        Long d = Long.parseLong(s);
        d++;
        return SerialiserNumero(String.valueOf(d), length);
    }

    public static String SerialiserNumero(String s, int length) {
        String serialised = s;
        for (int i = 0; i < length - s.length(); i++) {
            serialised = '0' + serialised;
        }
        return serialised;
    }


    public static String formatstring(List<String> l) {
        String s = "(";
        for (int i = 0; i < l.size(); i++) {
            String ch = l.get(i);
            if (i == l.size() - 1) {

                s += "'" + ch + "'";
            } else {
                s += "'" + ch + "'" + ",";
            }
        }
        s += ")";
        return s;
    }

    public static long differenceDate(Date date1, Date date2) {
        long UNE_HEURE = 60 * 60 * 1000L;
        return ((date2.getTime() - date1.getTime() + UNE_HEURE) / (UNE_HEURE * 24) + 1);
    }

    public static java.sql.Date parseStringToSqlDate(String date) {

        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date parsed = formatter.parse(date);
            return new java.sql.Date(parsed.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static byte[] read(ByteArrayInputStream bais) throws IOException {
        byte[] array = new byte[bais.available()];
        bais.read(array);

        return array;
    }

     public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
     
      /**
     *
     * @param prixHT the price without taxes
     * @param taxPourcentage pourcentage of taxes.
     * @return price with taxes scaled to two digits with rounding mode HALF_UP.
     *
     * <p>
     * For example passing 10.0 as #prixHT and 10.0 as #tvaPourcentage will
     * return 11.00
     */
    public static BigDecimal applyTaxesToPrice(BigDecimal prixHT, BigDecimal taxPourcentage) {
        BigDecimal taxValue = taxPourcentage.divide(BigDecimal.valueOf(100.0));
        prixHT.setScale(2, RoundingMode.HALF_UP);
        return prixHT.multiply(taxValue.add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
    }
    public static byte[] hashing(String originalString) throws NoSuchAlgorithmException {

       MessageDigest digest = MessageDigest.getInstance("SHA-384");
       byte[] hash = digest.digest(
               originalString.getBytes(StandardCharsets.UTF_8));
       return hash;
   }
}

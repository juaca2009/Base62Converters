package org.optimazed;

public class NormalBase62 {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(Long number){
        if (number == 0L){
            return "0";
        }
        StringBuilder result = new StringBuilder();
        while (number > 0){
            result.append(ALPHABET.charAt((int)(number % 62)));
            number = number / 62;
        }
        return result.reverse().toString();
    }

    public static Long decode(String code){
        Long result = 0L;
        int lenght = code.length();
        for(int i=0; i<lenght; i++){
            result = result + (long) Math.pow(62,i) * ALPHABET.indexOf(code.charAt(lenght-i-1));
        }
        return result;
    }
}

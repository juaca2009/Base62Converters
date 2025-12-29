package org.optimazed;

public class OptimazedBase62 {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] ALPHABET_ARRAY = ALPHABET.toCharArray();
    private static final int MAX_LENGTH = 11;
    private static final int[] CHAR_VALUES = new int[256];
    static {
        for (int i = 0; i < CHAR_VALUES.length; i++) {
            CHAR_VALUES[i] = -1;
        }
        for (int i = 0; i < ALPHABET.length(); i++) {
            char c = ALPHABET.charAt(i);
            CHAR_VALUES[c] = i;
        }
    }

    public static String encode(Long number){
        if (number == 0L){
            return "0";
        }
        char[] buffer = new char[MAX_LENGTH];
        int index = MAX_LENGTH;
        while (number > 0){
            int remainder = (int)(number % 62);
            buffer[--index] = ALPHABET_ARRAY[remainder];
            number = number / 62;
        }
        return new String(buffer, index, MAX_LENGTH-index);
    }

    public static Long decode(String code){
        Long result = 0L;
        for(int i=0; i<code.length();i++){
            char c = code.charAt(i);
            int value = CHAR_VALUES[c];

            if(value==-1){
                throw new IllegalArgumentException("Caracter invalido: " + c);
            }
            if(result > (Long.MAX_VALUE - value) / 62) {
                throw new ArithmeticException("Overflow en conversión");
            }
            result = result * 62 + value;
        }
        return result;
    }
}

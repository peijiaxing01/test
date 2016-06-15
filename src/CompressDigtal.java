
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 * 压缩数字工具
 */
public class CompressDigtal {
    
    static String key = "2016";
    
    static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z' };
    
    static HashMap<Character, Integer> map = new HashMap<Character, Integer>();
    
    static {
        map.put('0', 0);
        map.put('1', 1);
        map.put('2', 2);
        map.put('3', 3);
        map.put('4', 4);
        map.put('5', 5);
        map.put('6', 6);
        map.put('7', 7);
        map.put('8', 8);
        map.put('9', 9);
        map.put('a', 10);
        map.put('b', 11);
        map.put('c', 12);
        map.put('d', 13);
        map.put('e', 14);
        map.put('f', 15);
        map.put('g', 16);
        map.put('h', 17);
        map.put('i', 18);
        map.put('j', 19);
        map.put('k', 20);
        map.put('l', 21);
        map.put('m', 22);
        map.put('n', 23);
        map.put('o', 24);
        map.put('p', 25);
        map.put('q', 26);
        map.put('r', 27);
        map.put('s', 28);
        map.put('t', 29);
        map.put('u', 30);
        map.put('v', 31);
        map.put('w', 32);
        map.put('x', 33);
        map.put('y', 34);
        map.put('z', 35);
        map.put('A', 36);
        map.put('B', 37);
        map.put('C', 38);
        map.put('D', 39);
        map.put('E', 40);
        map.put('F', 41);
        map.put('G', 42);
        map.put('H', 43);
        map.put('I', 44);
        map.put('J', 45);
        map.put('K', 46);
        map.put('L', 47);
        map.put('M', 48);
        map.put('N', 49);
        map.put('O', 50);
        map.put('P', 51);
        map.put('Q', 52);
        map.put('R', 53);
        map.put('S', 54);
        map.put('T', 55);
        map.put('U', 56);
        map.put('V', 57);
        map.put('W', 58);
        map.put('X', 59);
        map.put('Y', 60);
        map.put('Z', 61);
    }
    
    public static String doCompress(Long articleId) {
        return doCompress(new BigDecimal(articleId), 62);
    }
    
    public static String unCompress(String articleIdStr) {
        return unCompress(articleIdStr, 62);
    }

    /**
     * 压缩函数，能够把数字压缩成为 shit 表示的进制位数
     * @param bigDecimal
     * @param shift
     * @return
     */
    public static String doCompress(BigDecimal bigDecimal, int shift) {
        bigDecimal = bigDecimal.multiply(new BigDecimal(key));
        BigDecimal divisor = new BigDecimal(shift);
        Deque<Character> numberDeque = new ArrayDeque<Character>();
        do {
            BigDecimal[] ba = bigDecimal.divideAndRemainder(divisor);
            bigDecimal = ba[0];
            numberDeque.addFirst(digits[ba[1].intValue()]);
        } while (bigDecimal.compareTo(BigDecimal.ZERO) > 0);
        StringBuilder builder = new StringBuilder();
        for (Character character : numberDeque) {
            builder.append(character);
        }
        return builder.toString();
    }

    /**
     * 解压缩
     * 
     * @param input
     * @param shift
     * @return
     */
    public static String unCompress(String input, int shift) {
        BigDecimal bigDecimal = new BigDecimal(0);
        int powerTimes = 0;
        for (int i = input.length() - 1; i >=0 ; i--) {
            // 获得每一位的数，从各位数开始
//            System.out.println((input.charAt(i)));
//            System.out.println(map.get(input.charAt(i)));
            BigDecimal temp = new BigDecimal(new Integer(map.get(input.charAt(i))));
            for (int j = 0; j < powerTimes; j++) {
                temp = temp.multiply(new BigDecimal(shift)); 
            }
            bigDecimal = bigDecimal.add(temp);
            powerTimes++;
        }
        bigDecimal = bigDecimal.divide(new BigDecimal(key));
        return bigDecimal.toPlainString();
    }
    
    public static void main(String[] args) {
        // for (int i = 0; i < 62; i++) {
        // String ns = "" + i;
        // BigDecimal bd = new BigDecimal(ns);
        // System.out.println(toUnsignedString(bd, 62));
        // }
        //String str = OrderIdGenerater.genrenateBfb(4225875);
        //System.out.println(str);
        System.out.println(doCompress(10031l));
        System.out.println(unCompress("1mHSw"));
        
        
    }

}

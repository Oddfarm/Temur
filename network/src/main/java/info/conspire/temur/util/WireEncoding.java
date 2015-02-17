package info.conspire.temur.util;

/**
 * Taken from Ion by Nillus
 */
/**
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <crowlie@hybridcore.net> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return Crowley.
 */
public class WireEncoding {
    public final static byte NEGATIVE = 72;
    public final static byte POSITIVE = 73;
    public final static int MAX_INTEGER_BYTE_AMOUNT = 6;

    public static byte[] encodeInt(int i) {
        byte[] wf = new byte[WireEncoding.MAX_INTEGER_BYTE_AMOUNT];

        int pos = 0;
        int numBytes = 1;
        int startPos = pos;
        int negativeMask = i >= 0 ? 0 : 4;
        i = Math.abs(i);

        wf[pos++] = (byte) (64 + (i & 3));

        for (i >>= 2; i != 0; i >>= WireEncoding.MAX_INTEGER_BYTE_AMOUNT) {
            numBytes++;
            wf[pos++] = (byte) (64 + (i & 0x3f));
        }

        wf[startPos] = (byte) (wf[startPos] | numBytes << 3 | negativeMask);
        byte[] bytes = new byte[numBytes];

        for (int x = 0; x < numBytes; x++) {
            bytes[x] = wf[x];
        }

        return bytes;
    }

    public static String encodeIntToStr(int i) {
        return new String(WireEncoding.encodeInt(i));
    }

    public static int[] decodeInt(byte[] bytes) {
        int[] ret = new int[2];
        int pos = 0;
        int v = 0;

        boolean negative = (bytes[pos] & 4) == 4;

        ret[1] = bytes[pos] >> 3 & 7;
        v = bytes[pos] & 3;

        pos++;

        int shiftAmount = 2;

        for (int b = 1; b < ret[1]; b++) {
            v |= (bytes[pos] & 0x3f) << shiftAmount;
            shiftAmount = 2 + 6 * b;
            pos++;
        }

        if (negative) {
            v *= -1;
        }

        ret[0] = v;
        return ret;
    }
}
package util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scan = new Scanner(System.in);

    public static int getInt() {
        return Integer.parseInt(scan.nextLine());
    }

    public static String getString() {
        return scan.nextLine();
    }
}

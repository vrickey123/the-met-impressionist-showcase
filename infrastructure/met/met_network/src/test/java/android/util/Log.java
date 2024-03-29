package android.util;

/**
 * Lightweight solution that does not require us move our tests to androidTest integration tests or
 * use RoboLectric just because a Log is used in a function.
 *
 * https://developer.android.com/training/testing/unit-testing/local-unit-tests#error-not-mocked
 * https://stackoverflow.com/questions/36787449/how-to-mock-method-e-in-log
 */
public class Log {
    public static int d(String tag, String msg) {
        System.out.println("DEBUG: " + tag + ": " + msg);
        return 0;
    }

    public static int i(String tag, String msg) {
        System.out.println("INFO: " + tag + ": " + msg);
        return 0;
    }

    public static int w(String tag, String msg) {
        System.out.println("WARN: " + tag + ": " + msg);
        return 0;
    }

    public static int e(String tag, String msg) {
        System.out.println("ERROR: " + tag + ": " + msg);
        return 0;
    }
}

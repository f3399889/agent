/**
 * @Author: Thread
 * @Date: 2022/3/17 13:33
 */
public class TestTime {
    public static void start(Object[] args) {
        System.err.println("11");
        System.err.println(args);
    }

    public static void end(Object[] args) {
        System.err.println("22");
        System.err.println(args);
    }

    public static void catch1(Throwable e) {
        System.err.println(e.getMessage());
    }
}

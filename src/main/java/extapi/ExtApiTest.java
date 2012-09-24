package extapi;

/**
 * @author Norman Fomferra
 */
public class ExtApiTest {
    public static void main(String[] args) throws InterruptedException {
        final double[] tile = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final int n = 1000000;

        test1(tile, n);
        test2(tile, n);
    }

    private static void test1(double[] tile, int n) {
        double result  = 0;
        final long t0 = System.nanoTime();
        for (int i = 0; i < n; i++) {
            result += ExtApi.INSTANCE.compute_tile(tile, tile.length);
        }
        final long t1 = System.nanoTime();
        final double ms = (t1 - t0) / (1000.0 * 1000.0);
        System.out.println("time = " + ms + " ms");
        System.out.println("result = " + result/n);
    }

    private static void test2(double[] tile, int n) {
        double result  = 0;
        final long t0 = System.nanoTime();
        for (int i = 0; i < n; i++) {
            result += ExtApi2.compute_tile(tile, tile.length);
        }
        final long t1 = System.nanoTime();
        final double ms = (t1 - t0) / (1000.0 * 1000.0);
        System.out.println("time = " + ms + " ms");
        System.out.println("result = " + result/n);
    }
}

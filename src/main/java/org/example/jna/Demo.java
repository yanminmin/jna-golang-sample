package org.example.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import org.example.jna.gotype.GoSlice;
import org.example.jna.gotype.GoString;
import org.example.utils.NativeProxy;
import org.example.utils.StdLib;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * window:
 * java -Djava.library.path=. -jar jna-golang-sample-jar-with-dependencies.jar "D:/src/jna-golang-sample/go/awesome.dll"
 * <p>
 * linux / macos
 * java -Djava.library.path=. -jar jna-golang-sample-jar-with-dependencies.jar "/home/jna-golang-sample/go/awesome.so"
 *
 * @author tangjialin on 2020-12-28.
 */
public class Demo {
    //mvn exec:java -Dexec.mainClass="org.example.jna.Demo"
    final public static String NAME = "awesome";
    final static private AwesomeInterface awesome = NativeProxy.load(NAME, AwesomeInterface.class);

    public static class TestThread extends Thread {
        private int index;
        private long size;

        public TestThread(int index) {
            this.index = index;
        }

        public void run() {
            for (; true; ) {
                size++;
                String text = "aaaaaaa";
                WString w = awesome.ReturnByteSlice(new GoString(text));
                if (w.toString().length() > 0 && size % 100 == 0) {
                    System.out.printf("%d %d \n", this.index, this.size);
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
//        System.out.printf("awesome.add(12, 99) = %s\n", awesome.add(12, 99));
//        System.out.printf("awesome.cosine(1.0) = %s\n", awesome.cosine(1.0));
//
//        // Call Sort
//        // First, prepare data array
//        long[] nums = new long[]{53, 11, 5, 2, 88};
//        // fill in the GoSlice class for type mapping
//        GoSlice slice = new GoSlice(nums);
//        awesome.sort(slice);
//        System.out.print("awesome.sort(53,11,5,2,88) = [");
//        long[] sorted = slice.data.getLongArray(0, nums.length);
//        StdLib.freeNativeHeap(slice.data);
//        for (int i = 0; i < sorted.length; i++) {
//            System.out.print(sorted[i] + " ");
//        }
//        System.out.println("]");

        // Call print
        String property = System.getProperty("os.arch");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(500);
        System.out.println(property);
        long start = System.currentTimeMillis();
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Thread thread = new TestThread(i);
            thread.start();
            threadList.add(thread);
        }
        for (int i = 0; i < 100; i++) {
            threadList.get(i).join();
        }
        long end = System.currentTimeMillis();
        System.out.println("子线程执行时长：" + (end - start));
//        for (; true; ) {
//            for (int i = 0; i < 10; i++) {
//                final int index = i;
//                fixedThreadPool.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        String text = "aaaaaaa";
//                        WString w  = awesome.ReturnByteSlice(new GoString(text));
//                        System.out.printf("ReturnByteSlice %s\n", w.toString());
//                    }
//                });
//            }
//        }

//        String value = awesome.echoString(new GoString(text));
//        System.out.println("awesome.echoString :" + value);

//        WString echo2 = awesome.echoWString(new GoString(text));
//        System.out.println("awesome.echoWString :" + echo2.toString());
//
//        GoString echo = awesome.echoGoString(new GoString(text));
//        System.out.println("awesome.echoGoString :" + echo.value);
    }

}

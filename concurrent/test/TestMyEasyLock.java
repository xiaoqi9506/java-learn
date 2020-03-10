import lock.MyEasyLock;

/**
 * 测试自定义锁
 */
public class TestMyEasyLock {

    /**
     * 多线程共享资源，倒数至0，每个数字只能使用一次
     */
    private static int num = 100;

    /**
     * synchronized锁对象
     */
    private static final Object obj = new Object();

    /**
     * 自定义锁
     */
    private static final MyEasyLock lock = new MyEasyLock();

    public static void main(String[] args) {

        //10个线程同时消耗共享资源，很明显会有线程安全问题，会重复打印部分数字
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lock.lock();
                while (num > 0) {
                    //解决方式1 synchronized同步代码块
                    //synchronized (obj) {
                    //    System.out.println("当前num值为：" + --num);
                    //}
                    try {
                        System.out.println("当前num值为：" + --num);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("程序异常");
                        lock.unlock();
                    }
                }
                lock.unlock();
            }).start();
        }
    }
}

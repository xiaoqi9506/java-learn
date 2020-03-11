import lock.MyReadWriteLock;

/**
 * 场景描述：网上图书馆里的书只能管理员进行修改，读者只能读。
 * 10个读者可以同时读同一本书，但是管理员修改书的时候任何人都不能读
 */
public class TestMyReadWriteLock {

    private static final MyReadWriteLock lock = new MyReadWriteLock();

    public static void main(String[] args) {
        for (int i = 0 ; i < 10 ; i++) {
           new Thread(new Reader((i+1) * 1000)).start();
        }
        new Thread(new Manager("管理员")).start();
    }

    /**
     * 管理员线程
     */
    static class Manager implements Runnable {

        private String name;

        public Manager(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name);
            while (true) {
                lock.writeLock().lock();
                System.out.println(Thread.currentThread().getName() +"加写锁成功，可以进行写操作");
                System.out.println(Thread.currentThread().getName() +"正在进行写操作。。。。。");
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() +"写操作异常");
                    lock.writeLock().unlock();
                }
                System.out.println(Thread.currentThread().getName() +"写操作完毕，释放写锁");
                lock.writeLock().unlock();
                //隔一段时间再进行写操作
                try {
                    Thread.sleep(30*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读者
     */
    static class Reader implements Runnable {

        /**
         * 隔一段时间再去读书
         */
        private long readTime;

        public Reader(long readTime) {
            this.readTime = readTime;
        }

        @Override
        public void run() {
            while (true) {
                lock.readLock().lock();
                System.out.println(Thread.currentThread().getName() + "加读锁成功，可以进行读操作");
                System.out.println(Thread.currentThread().getName() +"正在进行读操作。。。。。");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName() +"读操作异常，释放本身持有的读锁");
                    lock.readLock().unlock();
                }
                System.out.println(Thread.currentThread().getName() +"读操作完毕，释放本身持有的读锁");
                lock.readLock().unlock();
                //隔一段时间再进行写操作
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

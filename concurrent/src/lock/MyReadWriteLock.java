package lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 读写锁，如果没有写锁，都可以加读锁，只能一个线程加写锁；如果有写锁，
 *
 *
 */
public class MyReadWriteLock implements ReadWriteLock {

    /**
     * 写锁标识，0-无写锁，1-申请写锁，2-有写锁
     */
    private volatile int writeFlag;

    private final ReadLock readLock;

    private final WriteLock writeLock;

    private final Sync sync = new Sync();

    public MyReadWriteLock() {
        this.readLock = new ReadLock();
        this.writeLock = new WriteLock();
    }

    public boolean isWrite() {
        return writeFlag > 0;
    }

    public int getWriteFlag() {
        return writeFlag;
    }

    public void compareAndSetWriteFlag(int expect, int update) {
        unsafe.compareAndSwapInt(this, writeFlagOffset, expect, update);
    }

    public Sync getSync() {
        return sync;
    }

    class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected int tryAcquireShared(int arg) {
            int c = getState();
            if (getWriteFlag() > 0 && getExclusiveOwnerThread() != Thread.currentThread()) {//正在申请写锁或者已加写锁，不能再加读锁
                return -1;
            }
            compareAndSetState(getState(), getState() + 1);
            return 1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return compareAndSetState(getState(), getState() - 1);
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if (getWriteFlag() > 0 || getState() > 0) {//已加写锁或读锁，不能加写锁，得等待读锁或者写锁释放
                return false;
            }
            compareAndSetWriteFlag(getWriteFlag(), getWriteFlag() + 1);
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }

        @Override
        protected boolean tryRelease(int arg) {
            compareAndSetWriteFlag(getWriteFlag(), getWriteFlag() - 1);
            setExclusiveOwnerThread(null);
            return true;
        }
    }

    /**
     * 读锁
     */
    class ReadLock implements java.util.concurrent.locks.Lock {

        final Sync sync;

        public ReadLock() {
            sync = getSync();
        }

        @Override
        public void lock() {
            sync.acquireShared(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            sync.releaseShared(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    class WriteLock implements java.util.concurrent.locks.Lock {

        final Sync sync;

        public WriteLock() {
            this.sync = getSync();
        }

        @Override
        public void lock() {
            sync.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            sync.release(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    /**
     * 加读锁
     */
    public java.util.concurrent.locks.Lock readLock() {
        return readLock;
    }

    /**
     * 加写锁
     */
    public java.util.concurrent.locks.Lock writeLock() {
        return writeLock;
    }

    private static final Unsafe unsafe;
    private static final long writeFlagOffset;

    static {
        try {
            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            unsafe =  (Unsafe) theUnsafeInstance.get(Unsafe.class);
            writeFlagOffset = unsafe.objectFieldOffset(MyReadWriteLock.class.getDeclaredField("writeFlag"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}

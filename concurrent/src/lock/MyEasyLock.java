package lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 简单锁，继承自AbstractQueuedSynchronizer
 */
public class MyEasyLock extends AbstractQueuedSynchronizer implements Lock{

    /**
     * 加锁方法
     */
    public void lock() {
        //注意这里调用的是抽象类里的acquire方法，而不是重写的tryAcquire方法
        acquire(1);
    }

    /**
     * 需要重写抽象类的这个方法，因为抽象类里没有具体实现
     * 这个方法主要是加锁逻辑，可以在这里实现一些复杂的加锁逻辑
     * 我这里只是实现简单的独占锁，所以逻辑比较简单
     */
    @Override
    protected boolean tryAcquire(int arg) {
        if (getState() == 0 && compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    /**
     * 解锁方法
     */
    public void unlock() {
        //注意这里调用的是抽象类里的release方法，而不是重写的tryRelease方法
        release(1);
    }

    /**
     * 需要重写抽象类的这个方法，因为抽象类里没有具体实现
     * 这个方法主要是解锁逻辑，需要对加上的锁一个个解开
     */
    @Override
    protected boolean tryRelease(int arg) {
        if (getState() == 0) throw new IllegalMonitorStateException();
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }
}

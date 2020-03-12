package singleton;

/**
 * 单例模式：
 *
 * 在有些系统中，为了节省内存资源、保证数据内容的一致性，对某些类要求只能创建一个实例。
 */
public class Singleton {

    private  static volatile Singleton singleton = null;

    private Singleton() {

    }

    public static synchronized Singleton getSingleton() {
        if (null == singleton) {
            singleton = new Singleton();
        }
        return singleton;
    }
}

import java.util.Arrays;
import java.util.Scanner;

/**
 * 题目：输入磁盘容量，排序后输出
 *
 * 输入示例：
 * 3
 * 1T
 * 20M
 * 3G
 *
 * 输出示例：
 * 20M
 * 3G
 * 1T
 */
public class Main implements Comparable{

    private Integer m;

    private String v;

    public Main(Integer m, String v) {
        this.m = m;
        this.v = v;
    }

    /**
     * 重写compareTo方法，用于后续排序
     */
    @Override
    public int compareTo(Object o) {
        Main to = (Main) o;
        int compareV = compareV(this, to);//先比较容量
        if (compareV > 0) {
            return 1;
        }else if (compareV == 0) {
            return this.m.compareTo(to.m);//容量相同再比较大小
        }else {
            return -1;
        }
    }

    /**
     * 容量单位比较
     *
     * 0-相等, 1-大于，-1-小于
     */
    public int compareV(Main from, Main to) {
        //M<G<T
        switch (from.v) {
            case "T" : if ("T".equals(to.v)) return 0;return 1;
            case "G" : if ("T".equals(to.v)) {
                return -1;
            }else if ("G".equals(to.v)) {
                return 0;
            }else return 1;
            case "M" : if ("M".equals(to.v)) return 0;return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Main[] mains = new Main[n];
        scanner.nextLine();
        //输入
        for (int i = 0 ; i < n; i ++) {
            String inStr = scanner.nextLine();
            Integer m = Integer.valueOf(inStr.substring(0, inStr.length() - 1));
            String v = inStr.substring(inStr.length() - 1);
            mains[i] = new Main(m, v);
        }
        //排序
        Arrays.sort(mains);
        //输出
        for (int i = 0 ; i < n; i ++) {
            System.out.println(mains[i].m + mains[i].v);
        }
    }
}

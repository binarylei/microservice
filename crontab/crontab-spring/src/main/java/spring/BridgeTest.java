package spring;

/**
 * @author binarylei
 * @version 2019-10-31
 */
public class BridgeTest implements BridgeInterface<String> {

    public void sayHello(String content) {
        System.out.println(content);
    }
}

interface BridgeInterface<T> {
    void sayHello(T content);
}

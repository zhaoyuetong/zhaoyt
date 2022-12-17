import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MainProxy {
    public static void main(String[] args) {
        //创建目标接口实现
        ProxyInterface proxyInterface = new TargetProxy();
        //创建InvocationHandler对象
        InvocationHandler invocationHandler = new ProxyInvocationHandler(proxyInterface);
        //创建代理对象
        /**
         *     public static Object newProxyInstance(ClassLoader loader,
         *                                           Class<?>[] interfaces,
         *                                           InvocationHandler h)
         */
        ProxyInterface o = (ProxyInterface) Proxy.newProxyInstance(proxyInterface.getClass().getClassLoader(), proxyInterface.getClass().getInterfaces(), invocationHandler);
        float v = o.get(100.f);
        System.out.println("增强后结果："+v);
    }
}

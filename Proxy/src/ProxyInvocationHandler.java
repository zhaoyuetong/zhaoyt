import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler {

    private Object target;

    public ProxyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置增强");
        Object invoke = method.invoke(target, args);
        if (invoke!=null){
            Float p = (Float) invoke;
            p = p+100.0f;
            invoke = p;
        }
        System.out.println("后置增强");
        return invoke;
    }
}

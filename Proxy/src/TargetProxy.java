public class TargetProxy implements ProxyInterface{
    @Override
    public float get(float f) {
        System.out.println("目标对象结果："+f);
        f = 80.1f;
        return f;
    }
}

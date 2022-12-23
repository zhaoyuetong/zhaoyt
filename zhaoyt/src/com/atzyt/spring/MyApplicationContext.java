package com.atzyt.spring;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

//自定义spring容器
public class MyApplicationContext {
    private Class configClass;
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //定义单例池
    private ConcurrentHashMap<String,Object> singeltonObjects = new ConcurrentHashMap<>();
    public MyApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;
        //扫描->beandefinition->beandefinitionMap
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replace(".","/");
            System.out.println(path);
            ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        //com.atzyt.service.UserService
                        String fileNameNow = fileName.substring((fileName.lastIndexOf("\\")+1));
                        String s = fileNameNow.split("\\.")[0];
                        String className = new String(path.replace("/","."));
                        className = className+"."+s;
                        System.out.println(className+"-----");

//                        String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
//                        className = className.replace("\\", ".");
                        Class<?> loadclass = classLoader.loadClass(className);
                        if (loadclass.isAnnotationPresent(Component.class)) {
                            //获取bean名字
                            Component component = loadclass.getAnnotation(Component.class);
                            String beanNmae = component.value();
                            //bean
                            BeanDefinition beanDefinition = new BeanDefinition();
                            //设置类类型
                            beanDefinition.setType(loadclass);
                            //判断单例bean还是多例bean
                            if (loadclass.isAnnotationPresent(Scope.class)){
                                Scope annotation = loadclass.getAnnotation(Scope.class);
                                beanDefinition.setScope(annotation.value());
                            }else {
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanNmae,beanDefinition);
                        }
                    }
                    //System.out.println(fileName);
                }

            }
            //System.out.println(path);//扫描路径

        }

        //实例化单例Bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //判断作用域
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName,beanDefinition);
                singeltonObjects.put(beanName,bean);
            }
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class aclass = beanDefinition.getType();
        try {
            Constructor constructor = aclass.getConstructor();
            Object bean = constructor.newInstance();
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new NullPointerException();
        }else {
            //目前只考虑两种情况
            String scope = beanDefinition.getScope();
            //若为单例，从单例池获取
            if (scope.equals("singleton")){
                Object bean = singeltonObjects.get(beanName);
                if (bean==null) {
                    bean = createBean(beanName, beanDefinition);
                    singeltonObjects.put(beanName,bean);
                }
                return bean;
            }else {
                return createBean(beanName, beanDefinition);
            }
        }

    }
}

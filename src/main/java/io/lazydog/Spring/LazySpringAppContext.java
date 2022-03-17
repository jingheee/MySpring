package io.lazydog.Spring;

import io.lazydog.Spring.anoo.Component;
import io.lazydog.Spring.anoo.ComponentScan;
import io.lazydog.Spring.anoo.Scope;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LazySpringAppContext {
    private Class configClass;
    private Map<String,BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<>();
    private Map<String,Object>singletonObjects=new ConcurrentHashMap<>();
    @SneakyThrows
    public LazySpringAppContext(Class configClass){

        this.configClass = configClass;
        if (configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan annotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = annotation.value();
            path=path.replace(".","/");
            ClassLoader classLoader = LazySpringAppContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            System.out.println(file);
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File innerFile : files) {
                    String filename = innerFile.getAbsolutePath();
//                    System.out.println(filename);
                    if (filename.endsWith(".class")){

                        String className = filename.substring(filename.indexOf("io"), filename.indexOf(".class"));
                        className=className.replace("\\",".");
                        Class<?> clazz = classLoader.loadClass(className);


                        if (clazz.isAnnotationPresent(Component.class)){
                            Component component = clazz.getAnnotation(Component.class);
                            String beanName = component.value();

                            BeanDefinition beanDefinition = new BeanDefinition(); // bean 类型确认
                            beanDefinition.setType(clazz);
                            if (clazz.isAnnotationPresent(Scope.class)){
                                Scope scope = clazz.getAnnotation(Scope.class);
                                beanDefinition.setScope(scope.value());
                            }else {
                                // 单例
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName,beanDefinition);
                            System.out.println(clazz.getName());
                        }
                    }
                }
            }
        }
        beanDefinitionMap.keySet().forEach(key->{
            BeanDefinition beanDefinition = beanDefinitionMap.get(key);
            if (beanDefinition.getScope().equals("singleton")){
                Object singleton = createSingleton(key, beanDefinition);
                singletonObjects.put("key",singleton);
            }
        });
    }

    public Object createSingleton(String beanName,BeanDefinition beanDefinition){ // 创建实例

        return null;
    }

    public Object getBean(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new IllegalArgumentException();
        }else {
            String scope = beanDefinition.getScope();
            if (!"singleton".equals(scope)){
                return createSingleton(beanName,beanDefinition);
            }
        }


        return singletonObjects.get(beanName);
    }
}

package github.ztc.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SingletonManger {

    private static Map<String,Object> map = new ConcurrentHashMap<>();
    public static <T> T getSingletonInstance(Class<T> clazz){
        Object o = map.get(clazz.toString());
        if(o==null){
            synchronized (SingletonManger.class){
                if(o==null){
                    try {
                        o=clazz.newInstance();
                        map.put(clazz.toString(),o);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return clazz.cast(o);
    }
}

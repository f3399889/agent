/**
 * @Title: MonitorAgentClassLoader.java
 * @Package cn.wine.monitor.agent
 */

import java.net.URL;
import java.net.URLClassLoader;

/***********************************
 * @ClassName: MonitorAgentClassLoader.java
 * @Description: 自定义类加载器
 * @author: myemi
 * @createdAt: 2019年12月26日上午9:39:13
 ***********************************/

public class MonitorAgentClassLoader extends URLClassLoader {

    public MonitorAgentClassLoader(URL[] urls) {
        super(urls, MonitorAgentClassLoader.class.getClassLoader());
    }


    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        // 优先从parent（SystemClassLoader）里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
            return super.loadClass(name, resolve);
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            // ignore
        }
        return super.loadClass(name, resolve);
    }
}

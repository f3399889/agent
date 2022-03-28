import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * @Author: Thread
 * @Date: 2022/3/17 10:40
 */
public class AgentStarter {

    private static Logger logger = Logger.getLogger(AgentStarter.class.getName());

    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     * 并被同一个System ClassLoader装载
     * 被统一的安全策略(security policy)和上下文(context)管理
     * <p>
     * -javaagent:D:\1\starter-1.0-SNAPSHOT.jar=D:\1\lib -Dkey1=12 agentOps=key1=value1  System.getProperty("key1")=12
     */
    public static void premain(String agentOps, Instrumentation inst) throws IOException, ClassNotFoundException {
        logger.info("=========premain方法执行========");

        String key1 = System.getProperty("key1");

        // 获取所在lib路径
        File libDir = new File(agentOps);
        if (!libDir.exists() || !libDir.isDirectory()) {
            return;
        }

        File[] jarFiles = libDir.listFiles(pathName -> pathName.isFile() && pathName.getName().endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            return;
        }

        List<URL> libUrlList = new ArrayList<>(jarFiles.length);
        List<JarFile> jarFileList = new ArrayList<>(jarFiles.length);
        for (File jarFile : jarFiles) {
            // jar URL
            libUrlList.add(jarFile.toURI().toURL());

            // file -> JarFile
            JarFile jar = new JarFile(jarFile);
            jarFileList.add(jar);

            // javassist环境里面
//            inst.appendToBootstrapClassLoaderSearch(jar);
        }
        URL[] libUrls = libUrlList.toArray(new URL[libUrlList.size()]);
        ClassLoader agentClassLoader = new MonitorAgentClassLoader(libUrls);
        Class<IBoot> boot0Class = null;
        try {
            boot0Class = (Class<IBoot>) agentClassLoader.loadClass(IBoot.IMPL_CLASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(IBoot.class.getName() + "实现类未找到", e);
        }
        IBoot boot0;
        try {
            boot0 = boot0Class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("实现化" + boot0Class.getName() + "失败", e);
        }
        boot0.advisorMain(inst, jarFileList);
    }
}

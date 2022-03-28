import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @Author: Thread
 * @Date: 2022/3/17 12:27
 */
public class Boot implements IBoot {

    /**
     * @param inst        javassist 转换
     * @param jarFileList jar数组
     */
    @Override
    public void advisorMain(Instrumentation inst, List<JarFile> jarFileList) throws ClassNotFoundException {
        System.err.println("premain 真正启动");
        System.err.println("jar list:" + jarFileList.size());

        Class<?> testTime = this.getClass().getClassLoader().loadClass("TestTime");
        System.err.println(testTime);

        Class<?> transformer = this.getClass().getClassLoader().loadClass("MyTransformer");
        System.err.println(transformer);

        inst.addTransformer(new MyTransformer(), true);
    }
}


import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @Author: Thread
 * @Date: 2022/3/18 9:50
 */
public interface IBoot {

    /**
     * 实现类
     */
    String IMPL_CLASS = "Boot";

    /**
     * @param inst        javassist 转换
     * @param jarFileList jar数组
     */
    void advisorMain(Instrumentation inst, List<JarFile> jarFileList) throws ClassNotFoundException;
}

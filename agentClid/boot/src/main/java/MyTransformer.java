import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Author: Thread
 * @Date: 2022/3/17 12:33
 */
public class MyTransformer implements ClassFileTransformer {
    /**
     * 参数：
     * loader - 定义要转换的类加载器；如果是引导加载器，则为 null
     * className - 完全限定类内部形式的类名称和 The Java Virtual Machine Specification 中定义的接口名称。例如，"java/util/List"。
     * classBeingRedefined - 如果是被重定义或重转换触发，则为重定义或重转换的类；如果是类加载，则为 null
     * protectionDomain - 要定义或重定义的类的保护域
     * classfileBuffer - 类文件格式的输入字节缓冲区（不得修改）
     * 返回：
     * 一个格式良好的类文件缓冲区（转换的结果），如果未执行转换,则返回 null。
     * 抛出：
     * IllegalClassFormatException - 如果输入不表示一个格式良好的类文件
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // 拦截目标类
        if (!"cn/wine/monitor/demo/springboot1/TestPerson".equals(className) && !"org/apache/catalina/servlets/DefaultServlet".equals(className)) {
            return null;
        }
        try {
            // 默认拥有了当前包类加载器
            ClassPool pool = ClassPool.getDefault();

            // 加载执行jar包类加载器
            pool.appendClassPath(new LoaderClassPath(loader));

            // 加载start包类加载器
            pool.appendClassPath(new LoaderClassPath(AgentStarter.class.getClassLoader()));

            //借助JavaAssist工具，进行字节码插桩
            CtClass cc;
            if ("cn/wine/monitor/demo/springboot1/TestPerson".equals(className)) {
                cc = pool.get("cn.wine.monitor.demo.springboot1.TestPerson");
            } else {
                cc = pool.get("org.apache.catalina.servlets.DefaultServlet");
            }

            CtClass etype = pool.get(Exception.class.getName());

            // 拦截目标方法
            CtMethod[] declaredMethods = cc.getDeclaredMethods();
            for (CtMethod method : declaredMethods) {
                // 所有方法，统计耗时；请注意，需要通过`addLocalVariable`来声明局部变量
//                method.addLocalVariable("start", CtClass.longType);
//                method.insertBefore("start = System.currentTimeMillis();");
                String name = method.getName();
                String methodName = method.getLongName();
                //方法:getPerson
                System.err.println("方法:" + name);
                //方法:cn.wine.monitor.demo.springboot1.TestPerson.getPerson(java.lang.String,int)
                System.err.println("方法:" + methodName);
//                method.insertAfter("System.out.println(\"" + methodName + " cost: \" + (System" +
                //在目标方法前后，插入代码
                method.insertBefore("TestTime.start($args);");
                method.addCatch("{TestTime.catch1($e); throw $e;}", etype);
                method.insertAfter("TestTime.end($args);");
//                        ".currentTimeMillis() - start));");
            }
            return cc.toBytecode();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}

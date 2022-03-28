package cn.wine.monitor.demo.springboot1;

import org.apache.catalina.servlets.DefaultServlet;

import javax.servlet.http.HttpServlet;

/**
 * @Author: Thread
 * @Date: 2022/3/17 11:07
 */
public class Application {

    public static void main(String[] args) {
        TestPerson t = new TestPerson();
        t.getPerson("111", 111);

        t.personFly("222", 222);

        HttpServlet servlet = new DefaultServlet();
        servlet.getServletInfo();
    }
}

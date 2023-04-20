package top.cc.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("MyAgent: " + instrumentation + " is running!");
        MyTransformer transformer = new MyTransformer();
        instrumentation.addTransformer(transformer);


    }
}

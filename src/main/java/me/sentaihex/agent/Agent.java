package me.sentaihex.agent;

import me.sentaihex.client.SentaiHex;
import java.lang.instrument.Instrumentation;

public class Agent {

    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("[SentaiHex] Agent injected successfully!");

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000); // Đợi Minecraft load
                SentaiHex.INSTANCE = new SentaiHex();
                SentaiHex.INSTANCE.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "SentaiHex-Main");

        thread.setDaemon(true);
        thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
        thread.start();
    }

    public static void premain(String args, Instrumentation inst) {
        agentmain(args, inst);
    }
}
package org.sample.jmeter.compose;

import kg.apc.jmeter.threads.UltimateThreadGroup;
import kg.apc.jmeter.threads.UltimateThreadGroupGui;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.sampler.TestAction;
import org.apache.jmeter.sampler.gui.TestActionGui;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.timers.ConstantThroughputTimer;

public class TestPlanElements {
    public static ThreadGroup getSimpleThreadGroup(String name, int threads, int iterationsPerThread, int rampUpSeconds) {
        LoopController loopController = new LoopController();
        loopController.setLoops(iterationsPerThread);
        loopController.setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.initialize();

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName(name);
        threadGroup.setNumThreads(threads);
        threadGroup.setRampUp(rampUpSeconds);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

        return threadGroup;
    }

    public static UltimateThreadGroup getFixedPaceThreadGroup(String name) {
        LoopController loopController = new LoopController();
        loopController.setLoops(-1); // unlimited loops
        loopController.setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.initialize();

        UltimateThreadGroup threadGroup = new UltimateThreadGroup();
        threadGroup.setName(name);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS, UltimateThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, UltimateThreadGroupGui.class.getName());

        return threadGroup;
    }

    public static HTTPSamplerProxy getHttpSampler(String name, String domain, int port, String path, HttpMethod method) {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();

        httpSampler.setName(name);
        httpSampler.setDomain(domain);
        httpSampler.setPort(port);
        httpSampler.setPath(path);
        httpSampler.setMethod(method.toString());

        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

        return httpSampler;
    }

    public static TestAction getTestAction(String name) {
        TestAction testAction = new TestAction();

        testAction.setAction(TestAction.PAUSE);
        testAction.setDuration("0");
        testAction.setName(name);

        testAction.setProperty(TestElement.TEST_CLASS, TestAction.class.getName());
        testAction.setProperty(TestElement.GUI_CLASS, TestActionGui.class.getName());

        return testAction;
    }

    public static ConstantThroughputTimer getPerThreadTimer(Double threadThroughputPerMinute) {
        ConstantThroughputTimer timer = new ConstantThroughputTimer();

        //timer.setCalcMode(ConstantThroughputTimer.Mode.ThisThreadOnly.ordinal());
        timer.setProperty("calcMode", ConstantThroughputTimer.Mode.ThisThreadOnly.ordinal());
        //timer.setThroughput(threadThroughputPerMinute);
        timer.setProperty("throughput", threadThroughputPerMinute.toString());
        timer.setName("Constant Throughput Timer");

        timer.setProperty(TestElement.TEST_CLASS, ConstantThroughputTimer.class.getName());
        timer.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());

        return timer;
    }
}

enum HttpMethod {
    GET,
    POST
}


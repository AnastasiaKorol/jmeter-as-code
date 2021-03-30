package org.sample.jmeter.compose;

import kg.apc.jmeter.threads.UltimateThreadGroup;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.sampler.TestAction;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestPlan {
    private ListedHashTree rootHashTree;
    private org.apache.jmeter.testelement.TestPlan testPlan;

    public TestPlan() {
        testPlan = new org.apache.jmeter.testelement.TestPlan("Test Plan");
        testPlan.setEnabled(true);
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
        testPlan.setProperty(TestElement.TEST_CLASS, org.apache.jmeter.testelement.TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

        rootHashTree = new ListedHashTree();
        rootHashTree.add(testPlan);
    }

    public static void initJmeter(String jmeterPath) {
        JMeterUtils.setJMeterHome(jmeterPath);

        String jmeterBin = jmeterPath + File.separator + "bin";

        JMeterUtils.loadJMeterProperties(jmeterBin + File.separator + "jmeter.properties");
        JMeterUtils.loadProperties(jmeterBin + File.separator + "user.properties");
        JMeterUtils.initLocale();
    }

    public TestPlan addSimpleThreadGroup(int threads, int iterationsPerThread) {
        ThreadGroup threadGroup = TestPlanElements.getSimpleThreadGroup("Test", threads, iterationsPerThread, 1);
        HTTPSamplerProxy httpSampler = TestPlanElements.getHttpSampler("Google", "www.google.com", 80, "/", HttpMethod.GET);

        ListedHashTree threadGroupHashTree = (ListedHashTree)rootHashTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(httpSampler);

        return this;
    }

    public TestPlan addFixedPaceThreadGroup(Integer durationMinutes, Integer threads, Integer rampUpSecs, double throughputPerThread) {
        UltimateThreadGroup threadGroup = TestPlanElements.getFixedPaceThreadGroup("Test");
        HTTPSamplerProxy httpSampler = TestPlanElements.getHttpSampler("Google", "www.google.com", 80, "/", HttpMethod.GET);
        TestAction flowControl = TestPlanElements.getTestAction("Load pace");
        ConstantThroughputTimer timer = TestPlanElements.getPerThreadTimer(throughputPerThread);

        // Set load steps
        List<CollectionProperty> steps = new ArrayList<>();
        steps.add(getLoadStep(durationMinutes, threads, rampUpSecs));
        threadGroup.setData(new CollectionProperty("ultimatethreadgroupdata", steps));

        ListedHashTree threadGroupHashTree = (ListedHashTree)rootHashTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(flowControl).add(timer);
        threadGroupHashTree.add(httpSampler);

        return this;
    }

    public void save(String path) throws IOException {
        Files.createDirectories(Paths.get((new File(path)).getParent()));
        SaveService.saveTree(rootHashTree, new FileOutputStream(path));
    }

    private CollectionProperty getLoadStep(Integer durationMinutes, Integer threads, Integer rampUpSecs) {
        List<StringProperty> step = new ArrayList<>();

        Integer durationSecs = durationMinutes * 60;
        // threads delay rampUpSecs durationSecs shutDownSecs
        step.add(new StringProperty(threads.toString(), threads.toString()));
        step.add(new StringProperty("0", "0"));
        step.add(new StringProperty(rampUpSecs.toString(), rampUpSecs.toString()));
        step.add(new StringProperty(durationSecs.toString(), durationSecs.toString()));
        step.add(new StringProperty("1", "1"));

        return new CollectionProperty("123", step);
    }
}

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.report.config.ConfigurationException;
import org.apache.jmeter.report.dashboard.GenerationException;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private static final String SCENARIO_NAME = "sample";

    public static void main(String[] args) throws IOException, ConfigurationException, GenerationException {
        JmeterUtils.initJmeter();

        // Configure test elements
        TestPlan testPlan = TestPlanUtils.getTestPlan();
        ThreadGroup threadGroup = TestPlanUtils.getSimpleThreadGroup("Test", 1, 1);
        HTTPSamplerProxy httpSampler = TestPlanUtils.getHttpSampler("www.google.com", 80, "/", HttpMethod.GET);

        // Create tree
        HashTree rootHashTree = new HashTree();
        rootHashTree.add(testPlan);
        HashTree threadGroupHashTree = rootHashTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(httpSampler);

        // Generate jmx
        SaveService.saveTree(rootHashTree, new FileOutputStream("sample.jmx"));

        // Add logger
        rootHashTree.add(testPlan, JmeterUtils.getResultCollector(SCENARIO_NAME + ".csv"));

        // Run test
        StandardJMeterEngine jmeterEngine = JmeterUtils.getJmeterEngine(rootHashTree);
        System.out.println("Running test suite, please wait...\n");
        jmeterEngine.run();
        System.out.println("\n... Test suite has finished.");

        // Report
        ReportGenerator generator = new ReportGenerator(SCENARIO_NAME + ".csv", null);
        generator.generate();
        System.out.println("Ready");
    }
}
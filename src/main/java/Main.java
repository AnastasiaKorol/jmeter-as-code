import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.report.config.ConfigurationException;
import org.apache.jmeter.report.dashboard.GenerationException;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private static final String SCENARIO_NAME = "sample";

    public static void main(String[] args) throws IOException, ConfigurationException, GenerationException {
        JmeterUtils.initJmeter();

        HashTree hashTree = new HashTree();

        HTTPSamplerProxy httpSampler = TestPlanUtils.getHttpSampler("www.google.com", 80, "/", HttpMethod.GET);

        ThreadGroup threadGroup = TestPlanUtils.getSimpleThreadGroup("Test", 1, 1);

        // Test plan
        TestPlan testPlan = new TestPlan("MY TEST PLAN");
        testPlan.setEnabled(true);

        hashTree.add(testPlan);
        hashTree.add(testPlan, threadGroup).add(threadGroup, httpSampler);

        // Generate the JMX
        SaveService.saveTree(hashTree, new FileOutputStream("sample.jmx"));

        ResultCollector resultCollector = JmeterUtils.getResultCollector(SCENARIO_NAME + ".csv");

        hashTree.add(testPlan, resultCollector);

        // Run the Test Plan
        StandardJMeterEngine jmeterEngine = JmeterUtils.getJmeterEngine(hashTree);
        System.out.println("Running test suite, please wait...\n");
        jmeterEngine.run();
        System.out.println("\n... Test suite has finished.");

        // HTML Report
        ReportGenerator generator = new ReportGenerator(SCENARIO_NAME + ".csv", null);
        generator.generate();
        System.out.println("Ready");
    }
}
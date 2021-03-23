import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.report.config.ConfigurationException;
import org.apache.jmeter.report.dashboard.GenerationException;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private static final String SCENARIO_NAME = "sample";

    public static void main(String[] args) throws IOException, ConfigurationException, GenerationException {
        // Init
        JMeterUtils.setJMeterHome(getResourcePath("."));
        JMeterUtils.loadJMeterProperties(getResourcePath("apache-jmeter-5.4.1/bin/jmeter.properties"));
        JMeterUtils.loadProperties(getResourcePath("apache-jmeter-5.4.1/bin/user.properties"));
        JMeterUtils.setProperty("saveservice_properties", getResourcePath("saveservice.properties"));
        JMeterUtils.initLocale();

        HashTree hashTree = new HashTree();

        // HTTP Sampler
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setDomain("www.google.com");
        httpSampler.setPort(80);
        httpSampler.setPath("/");
        httpSampler.setMethod("GET");

        // Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setFirst(true);
        loopController.initialize();

        // Thread Group
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(1);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController(loopController);

        // Test plan
        TestPlan testPlan = new TestPlan("MY TEST PLAN");
        testPlan.setEnabled(true);

        hashTree.add(testPlan);
        hashTree.add(testPlan, threadGroup).add(threadGroup, httpSampler);

        //Generate the JMX
        SaveService.saveTree(hashTree, new FileOutputStream("sample.jmx"));

        ResultCollector resultCollector = Utils.getResultCollector(SCENARIO_NAME + ".csv");

        hashTree.add(testPlan, resultCollector);

        // Run the Test Plan
        StandardJMeterEngine jmeterEngine = Utils.getJmeterEngine(hashTree);
        System.out.println("Running test suite, please wait...\n");
        jmeterEngine.run();
        System.out.println("\n... Test suite has finished.");

        //We generate the HTML Report
        //Runtime.getRuntime().exec("/../bin/jmeter -g stresstest.csv -o " + REPORT_FOLDER);
        ReportGenerator generator = new ReportGenerator(SCENARIO_NAME + ".csv", null);
        generator.generate();
        System.out.println("Ready");
    }

    private static String getResourcePath(String name) {
        return Main.class.getClassLoader().getResource(name).getPath();
    }

    private static void exportResultsToHtml() {
        //        HtmlTemplateExporter htmlTemplateExporter = new HtmlTemplateExporter();
//
//        SampleContext sampleContext = new SampleContext();
//        File file = new File("report.txt");
//        Properties reportGenerationProperties = new Properties();
//        FileReader fileReader = new FileReader(new File("reportgenerator.properties"));
//        reportGenerationProperties.load(fileReader);
//        ReportGeneratorConfiguration reportGeneratorConfiguration =  ReportGeneratorConfiguration.loadFromProperties(reportGenerationProperties);
//
//
//        htmlTemplateExporter.export(sampleContext, file, reportGeneratorConfiguration);
    }
}
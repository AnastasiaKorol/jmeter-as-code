import org.apache.commons.cli.ParseException;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final String SCENARIO_NAME = "sample";

    public static void main(String[] args) throws IOException, ParseException {
        // Depends a lot on jmeter-maven-plugin realization
        System.out.println("Trying to guess jmeter path");
        String jmeterPath = ArgsParser.getJmeterPathFromArgs(args);
        System.out.println(jmeterPath);
        JmeterUtils.initJmeter(jmeterPath);

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
        Files.createDirectories(Paths.get("target/compiled-tests"));
        SaveService.saveTree(rootHashTree, new FileOutputStream("target/compiled-tests/" + SCENARIO_NAME + ".jmx"));
    }

}
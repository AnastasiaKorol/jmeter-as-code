import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;

public class TestPlanUtils {
    public static ThreadGroup getSimpleThreadGroup(String name, Integer threads, Integer rampUpSeconds, HTTPSamplerProxy sampler) {
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
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

    public static HTTPSamplerProxy getHttpSampler(String domain, Integer port, String path, HttpMethod method) {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setName("Google");
        httpSampler.setDomain(domain);
        httpSampler.setPort(port);
        httpSampler.setPath(path);
        httpSampler.setMethod(method.toString());
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

        return httpSampler;
    }
}

enum HttpMethod {
    GET,
    POST
}

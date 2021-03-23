import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.threads.ThreadGroup;

public class TestPlanUtils {
    public static ThreadGroup getSimpleThreadGroup(String name, Integer threads, Integer rampUpSeconds) {
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setFirst(true);
        loopController.initialize();

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(threads);
        threadGroup.setRampUp(rampUpSeconds);
        threadGroup.setSamplerController(loopController);

        return threadGroup;
    }

    public static HTTPSamplerProxy getHttpSampler(String domain, Integer port, String path, HttpMethod method) {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setDomain(domain);
        httpSampler.setPort(port);
        httpSampler.setPath(path);
        httpSampler.setMethod(method.toString());

        return httpSampler;
    }
}

enum HttpMethod {
    GET,
    POST
}

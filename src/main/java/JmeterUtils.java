import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class JmeterUtils {
    public static ResultCollector getResultCollector(String logFileName) {
        Summariser summariser = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summariser = new Summariser(summariserName);
        }
        summariser.setEnabled(true);

        ResultCollector resultCollector = new ResultCollector(summariser);
        resultCollector.setFilename(logFileName);
        resultCollector.setEnabled(true);

        return resultCollector;
    }

    public static StandardJMeterEngine getJmeterEngine(HashTree testPlanTree) {
        StandardJMeterEngine jmeterEngine = new StandardJMeterEngine();
        jmeterEngine.configure(testPlanTree);
        return jmeterEngine;
    }

    public static void initJmeter() {
        JMeterUtils.setJMeterHome(getResourcePath("."));
        JMeterUtils.loadJMeterProperties(getResourcePath("apache-jmeter-5.4.1/bin/jmeter.properties"));
        JMeterUtils.loadProperties(getResourcePath("apache-jmeter-5.4.1/bin/user.properties"));
        JMeterUtils.setProperty("saveservice_properties", getResourcePath("saveservice.properties"));
        JMeterUtils.initLocale();
    }

    private static String getResourcePath(String name) {
        return Main.class.getClassLoader().getResource(name).getPath();
    }
}
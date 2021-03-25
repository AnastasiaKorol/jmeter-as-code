import org.apache.jmeter.util.JMeterUtils;

import java.io.File;

public class JmeterUtils {
    public static void initJmeter(String jmeterPath) {
        JMeterUtils.setJMeterHome(jmeterPath);
        String jmeterBin = jmeterPath + File.separator + "bin";
        JMeterUtils.loadJMeterProperties(jmeterBin + File.separator + "jmeter.properties");
        JMeterUtils.loadProperties(jmeterBin + File.separator + "user.properties");
        // use custom saveservice
        JMeterUtils.setProperty("saveservice_properties",
                Main.class.getClassLoader().getResource("saveservice.properties").getPath());
        JMeterUtils.initLocale();
    }
}
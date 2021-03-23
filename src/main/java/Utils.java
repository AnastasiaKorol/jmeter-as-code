//package com.company.stresstest.util;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class Utils {
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
}
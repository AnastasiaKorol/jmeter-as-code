import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

public class Main {
    private final static String JMX_FOLDER = "target/compiled-tests";

    public static void main(String[] args) throws IOException, ParseException {
        String jmeterPath = new ArgsParser(args).getJmeterPath();

        System.out.println("Jmeter path " + jmeterPath);
        System.out.println("Created jmx will be saved to " + JMX_FOLDER);

        ScenarioManager sm = new ScenarioManager(jmeterPath);
        sm.getGoogle(2, 2).save(JMX_FOLDER + File.separator + "sample.jmx");
        sm.getGoogleWithFixedPace(10, 2, 1, 0.5)
                .save(JMX_FOLDER + File.separator + "sample_fixedpace.jmx"); // get google with 1 rps
    }
}
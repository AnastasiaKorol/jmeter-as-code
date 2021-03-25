import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ArgsParser {
    private final static String configOptionShortName = "c";
    private final static String configOptionLongName = "config";
    private final static String jmeterOptionShortName = "j";
    private final static String jmeterOptionLongName = "jmeter-folder";

    public static String getJmeterPathFromArgs(String[] args) throws IOException, ParseException {
        Options options = initOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(jmeterOptionLongName)) {
            return cmd.getOptionValue(jmeterOptionLongName);
        }
        else if (cmd.hasOption(configOptionLongName)) {
            return guessJmeterPath(cmd.getOptionValue(configOptionLongName));
        }
        else {
            throw new ParseException(
                    String.format("Specify jmeter folder (--%s) or jmeter-maven-plugin config (--%s)",
                    jmeterOptionLongName,
                    configOptionLongName));
        }
    }

    private static Options initOptions() {
        Options options = new Options();

        Option jmeter = new Option(jmeterOptionShortName, jmeterOptionLongName, true, "jmeter path");
        jmeter.setRequired(false);
        options.addOption(jmeter);

        Option config = new Option(configOptionShortName, configOptionLongName, true, "jmeter-maven-plugin config");
        config.setRequired(false);
        options.addOption(config);

        return options;
    }

    private static String guessJmeterPath(String configPath) throws IOException {
        String jsonString = FileUtils.readFileToString(new File(configPath), "UTF-8");

        // json -> configurations[0] -> jmeterDirectoryPath
        JSONObject obj = new JSONObject(jsonString);
        JSONObject configuration = (JSONObject)obj.getJSONArray("configurations").get(0);
        String jmeterPath = configuration.get("jmeterDirectoryPath").toString();

        return jmeterPath;
    }
}

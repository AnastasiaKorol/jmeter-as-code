import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ArgsParser {
    private final String CONFIG_SHORT_NAME = "c";
    private final String CONFIG_LONG_NAME = "config";

    private final String JMETER_SHORT_NAME = "j";
    private final String JMETER_LONG_NAME = "jmeter-folder";

    private String jmeterPath;

    public ArgsParser(String[] args) throws ParseException, IOException {
        Options options = initOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(JMETER_LONG_NAME)) {
            jmeterPath = cmd.getOptionValue(JMETER_LONG_NAME);
        }
        else if (cmd.hasOption(CONFIG_LONG_NAME)) {
            jmeterPath = guessJmeterPath(cmd.getOptionValue(CONFIG_LONG_NAME));
        }
        else {
            throw new ParseException(
                    String.format("Specify jmeter folder (--%s) or jmeter-maven-plugin config (--%s)",
                            JMETER_LONG_NAME,
                            CONFIG_LONG_NAME));
        }
    }

    public String getJmeterPath() {
        return jmeterPath;
    }

    private Options initOptions() {
        Options options = new Options();

        Option jmeter = new Option(JMETER_SHORT_NAME, JMETER_LONG_NAME, true, "jmeter path");
        jmeter.setRequired(false);
        options.addOption(jmeter);

        Option config = new Option(CONFIG_SHORT_NAME, CONFIG_LONG_NAME, true, "jmeter-maven-plugin config");
        config.setRequired(false);
        options.addOption(config);

        return options;
    }

    /**
     * Try to guess jmeter path from jmeter-maven-plugin config, suppose jmeter was already downloaded and configured
     * @param configPath
     * @return
     * @throws IOException
     */
    private String guessJmeterPath(String configPath) throws IOException {
        String jsonString = FileUtils.readFileToString(new File(configPath), "UTF-8");

        // json -> configurations[0] -> jmeterDirectoryPath
        JSONObject obj = new JSONObject(jsonString);
        JSONObject configuration = (JSONObject)obj.getJSONArray("configurations").get(0);
        String jmeterPath = configuration.get("jmeterDirectoryPath").toString();

        return jmeterPath;
    }
}

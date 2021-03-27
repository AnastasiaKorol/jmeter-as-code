import org.sample.jmeter.compose.TestPlan;

public class ScenarioManager {
    public ScenarioManager(String jmeterPath) {
        TestPlan.initJmeter(jmeterPath);
    }

    public TestPlan getGoogle(int threads, int iterationsPerThread) {
        return new TestPlan().addSimpleThreadGroup(threads, iterationsPerThread);
    }

    public TestPlan getGoogleWithFixedPace(int durationMinutes, int threads, int rampUpSecs, double throughputPerThread) {
        return new TestPlan().addFixedPaceThreadGroup(durationMinutes, threads, rampUpSecs, throughputPerThread);
    }
}

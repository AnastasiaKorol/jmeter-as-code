# jmeter-as-code
This project demonstrates how to write tests with jmeter API. Run
> mvn verify

this will
- download jmeter 5.4.1 with all dependecies
- compile code and create jmx files in target/compiled-tests
- run sample.jmx
- build jmeter html report
Use maven profiles to run other jmx file

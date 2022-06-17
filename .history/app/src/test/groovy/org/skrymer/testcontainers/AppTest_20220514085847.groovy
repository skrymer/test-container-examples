package org.skrymer.testcontainers

import spock.lang.Specification

class AppTest extends Specification {

    public static void sampleInitFunction(Connection connection) throws SQLException {
    // e.g. run schema setup or Flyway/liquibase/etc DB migrations here...
        
    }

    def "application has a greeting"() {
        setup:
        def app = new App()

        when:
        def result = app.greeting

        then:
        result != null
    }

}

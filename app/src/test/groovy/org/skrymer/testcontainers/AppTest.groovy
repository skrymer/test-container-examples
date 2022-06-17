package org.skrymer.testcontainers


import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class AppTest extends Specification {

  @Shared
  PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:9.6.12"))
      .withDatabaseName("foo")
      .withUsername("foo")
      .withPassword("secret")

  /**
   * Create the DB with a table
   * Use Flyway or Liquibase in real life
   * @return
   */
  def setupSpec() {
    var stmt = createDataSource().getConnection().createStatement()
    stmt.execute("CREATE DATABASE TEST_DB")
    stmt.execute("""CREATE TABLE Persons (
        PersonID SERIAL,
        LastName varchar(255),
        FirstName varchar(255),
        Address varchar(255),
        City varchar(255)
        );
        INSERT INTO Persons(LastName, FirstName, Address, City) 
        values('Nielsen', 'Sonni', '32 doyle place', 'Bribane');
    """)
  }

  def "we can access the DB"() {
    expect: 'we can query the DB'
    def result = createDataSource()
        .getConnection()
        .createStatement()
        .executeQuery("select * from Persons where FirstName = 'Sonni'")

    result.next()

    result.getString("FirstName") == "Sonni"
  }

  def createDataSource() {
    return new SingleConnectionDataSource(postgreSQLContainer.jdbcUrl, "foo", "secret", false);
  }
}

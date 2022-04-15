package org.hisp.dhis.integration.camel;

import org.hisp.dhis.api.v2_37_4.model.OrganisationUnit;
import org.hisp.dhis.api.v2_37_4.model.OrganisationUnitLevel;
import org.hisp.dhis.api.v2_37_4.model.WebMessage;
import org.hisp.dhis.integration.sdk.Dhis2Client;
import org.hisp.dhis.integration.sdk.Dhis2ClientBuilder;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public final class Environment
{
    private Environment()
    {

    }

    private static final Network NETWORK = Network.newNetwork();

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    private static final GenericContainer<?> DHIS2_CONTAINER;

    private static final Dhis2Client DHIS2_CLIENT;

    public static final String ORG_UNIT_ID;

    static
    {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse( "postgis/postgis:12-3.2-alpine" ).asCompatibleSubstituteFor( "postgres" ) )
            .withDatabaseName( "dhis2" )
            .withNetworkAliases( "db" )
            .withUsername( "dhis" )
            .withPassword( "dhis" ).withNetwork( NETWORK );

        POSTGRESQL_CONTAINER.start();

        DHIS2_CONTAINER = new GenericContainer<>(
            "dhis2/core:2.37.4-tomcat-8.5.34-jre8-alpine" )
            .dependsOn( POSTGRESQL_CONTAINER )
            .withClasspathResourceMapping( "dhis.conf", "/DHIS2_home/dhis.conf", BindMode.READ_WRITE )
            .withNetwork( NETWORK ).withExposedPorts( 8080 ).waitingFor( new HttpWaitStrategy().forStatusCode( 200 ) )
            .withEnv( "WAIT_FOR_DB_CONTAINER", "db" + ":" + 5432 + " -t 0" );

        DHIS2_CONTAINER.start();

        DHIS2_CLIENT = Dhis2ClientBuilder.newClient(
            "http://" + Environment.getDhis2Container().getHost() + ":" + Environment.getDhis2Container()
                .getFirstMappedPort() + "/api", "admin", "district" ).build();

        importMetaData();
        ORG_UNIT_ID = createOrgUnit();
        createOrgUnitLevel();
        addOrgUnitToUser(ORG_UNIT_ID);
    }

    private static void importMetaData()
    {
        String metaData;
        try
        {
            metaData = new String(
                Thread.currentThread().getContextClassLoader().getResourceAsStream( "MLAG00_1.2.1_DHIS2.37.json" )
                    .readAllBytes(),
                Charset.defaultCharset() );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }

        DHIS2_CLIENT.post( "metadata" ).withResource( metaData ).withParameter( "atomicMode", "NONE" ).transfer();
    }

    private static String createOrgUnit()
    {
        OrganisationUnit organisationUnit = new OrganisationUnit().withName( "Acme" ).withShortName( "Acme" )
            .withOpeningDate( new Date() );

        return DHIS2_CLIENT.post( "organisationUnits" ).withResource( organisationUnit ).transfer()
            .returnAs( WebMessage.class ).getResponse().get().get( "uid" );
    }

    private static void createOrgUnitLevel()
    {
        OrganisationUnitLevel organisationUnitLevel = new OrganisationUnitLevel().withName( "Level 1" )
            .with( "level", 1 );
        DHIS2_CLIENT.post( "filledOrganisationUnitLevels" ).withResource( organisationUnitLevel ).transfer();
    }

    private static void addOrgUnitToUser( String orgUnitId )
    {
        DHIS2_CLIENT.post( "users/M5zQapPyTZI/organisationUnits/{organisationUnitId}", orgUnitId ).transfer();
    }

    public static GenericContainer<?> getDhis2Container()
    {
        return DHIS2_CONTAINER;
    }

    public static Dhis2Client getDhis2Client()
    {
        return DHIS2_CLIENT;
    }
}

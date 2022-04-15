package org.hisp.dhis.integration.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.hisp.dhis.api.v2_37_4.model.OrganisationUnit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetDhis2TestCase extends AbstractDhis2TestSupport
{
    @Test
    public void testCollectionEndpoint()
    {
        Iterator<OrganisationUnit> organisationUnitIterator = requestBody( "direct://collection", null );
        List<OrganisationUnit> organisationUnits = new ArrayList<>();
        organisationUnitIterator.forEachRemaining( organisationUnits::add );

        assertTrue( organisationUnits.size() >= 1 );
    }

    @Test
    public void testResourceEndpoint()
    {
        OrganisationUnit organisationUnit = requestBodyAndHeaders( "direct://resource", null,
            Map.of( "orgUnitId", Environment.ORG_UNIT_ID ) );
        assertEquals( Environment.ORG_UNIT_ID, organisationUnit.getId().get() );
    }

    @Override
    protected RouteBuilder createRouteBuilder()
    {
        return new RouteBuilder()
        {
            public void configure()
            {
                from( "direct://collection" )
                    .to(
                        "dhis2://get/collection?path=organisationUnits&paging=true&itemType=org.hisp.dhis.api.v2_37_4.model.OrganisationUnit" );

                from( "direct://resource" )
                    .toD( "dhis2://get/resource?path=organisationUnits/${header.orgUnitId}" )
                    .unmarshal().json( OrganisationUnit.class );
            }
        };
    }
}

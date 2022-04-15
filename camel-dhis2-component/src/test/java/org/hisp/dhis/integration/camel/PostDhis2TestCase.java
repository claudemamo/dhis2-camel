package org.hisp.dhis.integration.camel;

import org.apache.camel.builder.RouteBuilder;
import org.hisp.dhis.api.v2_37_4.model.DescriptiveWebMessage;
import org.hisp.dhis.api.v2_37_4.model.OrganisationUnit;
import org.hisp.dhis.api.v2_37_4.model.WebMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostDhis2TestCase extends AbstractDhis2TestSupport
{
    @Test
    public void testResourceEndpoint()
    {
        WebMessage webMessage = requestBody( "direct://resource",
            new OrganisationUnit().withName( "Foo" ).withShortName( "Foo" ).withOpeningDate( new Date() ) );
        assertEquals( DescriptiveWebMessage.Status.OK, webMessage.getStatus().get() );
    }

    @Override
    protected RouteBuilder createRouteBuilder()
    {
        return new RouteBuilder()
        {
            public void configure()
            {
                from( "direct://resource" )
                    .toD( "dhis2://post/resource?path=organisationUnits&inBody=resource" )
                    .unmarshal().json( WebMessage.class );
            }
        };
    }
}

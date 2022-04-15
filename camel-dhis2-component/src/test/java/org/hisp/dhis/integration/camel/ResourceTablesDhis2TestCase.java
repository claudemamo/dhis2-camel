package org.hisp.dhis.integration.camel;

import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

public class ResourceTablesDhis2TestCase extends AbstractDhis2TestSupport
{
    @Test
    public void testAnalyticsEndpoint()
    {
        requestBody( "direct://analytics", null );
    }

    @Override
    protected RouteBuilder createRouteBuilder()
    {

        return new RouteBuilder()
        {
            public void configure()
            {
                from( "direct://analytics" )
                    .to( "dhis2://resourceTables/analytics?skipAggregate=false&skipEvents=false&lastYears=2" );
            }
        };
    }
}

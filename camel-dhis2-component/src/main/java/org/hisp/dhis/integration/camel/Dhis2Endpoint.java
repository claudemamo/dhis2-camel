package org.hisp.dhis.integration.camel;

import java.util.Map;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.component.AbstractApiEndpoint;
import org.apache.camel.support.component.ApiMethod;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import org.hisp.dhis.integration.camel.api.Dhis2Get;
import org.hisp.dhis.integration.camel.api.Dhis2Post;
import org.hisp.dhis.integration.camel.api.Dhis2ResourceTables;
import org.hisp.dhis.integration.camel.internal.Dhis2ApiCollection;
import org.hisp.dhis.integration.camel.internal.Dhis2ApiName;
import org.hisp.dhis.integration.camel.internal.Dhis2Constants;
import org.hisp.dhis.integration.camel.internal.Dhis2PropertiesHelper;
import org.hisp.dhis.integration.sdk.Dhis2Client;

/**
 * Dhis2 component which does bla bla.
 * <p>
 * TODO: Update one line description above what the component does.
 */
@UriEndpoint( firstVersion = "1.0-SNAPSHOT", scheme = "dhis2", title = "DHIS2", syntax = "dhis2:methodName",
    apiSyntax = "apiName/methodName",
    category = { Category.API } )
public class Dhis2Endpoint extends AbstractApiEndpoint<Dhis2ApiName, Dhis2Configuration>
{

    @UriParam
    private final Dhis2Configuration configuration;

    // TODO create and manage API proxy
    private Object apiProxy;

    public Dhis2Endpoint( String uri, Dhis2Component component,
        Dhis2ApiName apiName, String methodName, Dhis2Configuration endpointConfiguration )
    {
        super( uri, component, apiName, methodName, Dhis2ApiCollection.getCollection().getHelper( apiName ),
            endpointConfiguration );
        this.configuration = endpointConfiguration;
    }

    public Producer createProducer()
        throws Exception
    {
        return new Dhis2Producer( this );
    }

    public Consumer createConsumer( Processor processor )
        throws Exception
    {
        // make sure inBody is not set for consumers
        if ( inBody != null )
        {
            throw new IllegalArgumentException( "Option inBody is not supported for consumer endpoint" );
        }
        final Dhis2Consumer consumer = new Dhis2Consumer( this, processor );
        // also set consumer.* properties
        configureConsumer( consumer );
        return consumer;
    }

    @Override
    protected ApiMethodPropertiesHelper<Dhis2Configuration> getPropertiesHelper()
    {
        return Dhis2PropertiesHelper.getHelper( getCamelContext() );
    }

    protected String getThreadProfileName()
    {
        return Dhis2Constants.THREAD_PROFILE_NAME;
    }

    @Override
    protected void afterConfigureProperties()
    {
        Dhis2Client dhis2Client = this.getClient();
        switch ( apiName )
        {
        case GET:
            apiProxy = new Dhis2Get( dhis2Client );
            break;
        case POST:
            apiProxy = new Dhis2Post( dhis2Client );
            break;
        case RESOURCE_TABLES:
            apiProxy = new Dhis2ResourceTables( dhis2Client );
            break;
        default:
            throw new IllegalArgumentException( "Invalid API name " + apiName );
        }
    }

    @Override
    public Object getApiProxy( ApiMethod method, Map<String, Object> args )
    {
        return apiProxy;
    }

    protected Dhis2Client getClient()
    {
        return ((Dhis2Component) this.getComponent()).getClient( this.configuration );
    }
}

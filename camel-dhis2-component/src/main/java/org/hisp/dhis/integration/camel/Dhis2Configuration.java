package org.hisp.dhis.integration.camel;

import org.apache.camel.spi.Configurer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;
import org.hisp.dhis.integration.camel.internal.Dhis2ApiName;
import org.hisp.dhis.integration.sdk.Dhis2Client;

@UriParams
@Configurer
public class Dhis2Configuration
{
    @UriParam( description = "Base API URL" )
    private String baseApiUrl;

    @UriParam( description = "Username" )
    private String username;

    @UriParam( description = "Password" )
    private String password;

    @UriPath( description = "API name" )
    @Metadata(
        required = true
    )
    private Dhis2ApiName apiName;

    @UriPath( description = "Method name" )
    @Metadata(
        required = true
    )
    private String methodName;

    @UriParam(
        label = "advanced",
        description = "To use the custom client"
    )
    private Dhis2Client client;

    public String getBaseApiUrl()
    {
        return baseApiUrl;
    }

    public void setBaseApiUrl( String baseApiUrl )
    {
        this.baseApiUrl = baseApiUrl;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public Dhis2ApiName getApiName()
    {
        return apiName;
    }

    public void setApiName( Dhis2ApiName apiName )
    {
        this.apiName = apiName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName( String methodName )
    {
        this.methodName = methodName;
    }

    public Dhis2Client getClient()
    {
        return client;
    }

    public void setClient( Dhis2Client dhis2Client )
    {
        this.client = dhis2Client;
    }
}

package org.hisp.dhis.integration.camel.api;

import org.hisp.dhis.integration.sdk.Dhis2Client;
import org.hisp.dhis.integration.sdk.api.operation.GetOperation;
import org.hisp.dhis.integration.sdk.api.operation.PostOperation;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Sample API used by Dhis2 Component whose method signatures are read from Java source.
 */
public class Dhis2Post
{
    private final Dhis2Client dhis2Client;

    public Dhis2Post( Dhis2Client dhis2Client )
    {
        this.dhis2Client = dhis2Client;
    }

    public InputStream resource( String path, Object resource, Map<String, List<String>> queryParams )
    {
        PostOperation postOperation = dhis2Client.post( path );
        if ( queryParams != null )
        {
            for ( Map.Entry<String, List<String>> queryParam : queryParams.entrySet() )
            {
                for ( String queryValue : queryParam.getValue() )
                {
                    postOperation.withParameter( queryParam.getKey(), queryValue );
                }
            }
        }

        if ( resource != null )
        {
            postOperation.withResource( resource );
        }

        return postOperation.transfer().read();

    }
}

package org.hisp.dhis.integration.camel.api;

import org.hisp.dhis.api.v2_37_4.model.OrganisationUnit;
import org.hisp.dhis.integration.sdk.Dhis2Client;
import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.operation.PagingCollectOperation;
import org.hisp.dhis.integration.sdk.api.operation.GetOperation;
import org.hisp.dhis.integration.sdk.internal.IterableDhis2Response;
import org.hisp.dhis.integration.sdk.internal.LazyIterableDhis2Response;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Sample API used by Dhis2 Component whose method signatures are read from Java source.
 */
public class Dhis2Get
{
    private final Dhis2Client dhis2Client;

    public Dhis2Get( Dhis2Client dhis2Client )
    {
        this.dhis2Client = dhis2Client;
    }

    public InputStream resource( String path, String fields, String filter, Map<String, List<String>> queryParams )
    {
        GetOperation getOperation = dhis2Client.get( path );
        if ( fields != null )
        {
            getOperation.withFields( fields );
        }

        if ( filter != null )
        {
            getOperation.withFilter( filter );
        }

        if ( queryParams != null )
        {
            for ( Map.Entry<String, List<String>> queryParam : queryParams.entrySet() )
            {
                for ( String queryValue : queryParam.getValue() )
                {
                    getOperation.withParameter( queryParam.getKey(), queryValue );
                }
            }
        }

        return getOperation.withParameter( "paging", "false" ).transfer().read();
    }

    public <T> Iterator<T> collection( String path, String itemType, Boolean paging, String fields, String filter )
    {
        GetOperation getOperation = dhis2Client.get( path );
        if ( fields != null )
        {
            getOperation.withFields( fields );
        }

        if ( filter != null )
        {
            getOperation.withFilter( filter );
        }
        try
        {
            if ( paging == null || paging )
            {
                LazyIterableDhis2Response lazyIterableDhis2Response = getOperation.withPaging().transfer();
                if ( itemType == null )
                {
                    return (Iterator<T>) lazyIterableDhis2Response
                        .returnAs( Map.class, path )
                        .iterator();
                }
                else
                {
                    return (Iterator<T>) lazyIterableDhis2Response
                        .returnAs( Class.forName( itemType ), path )
                        .iterator();
                }
            }
            else
            {
                IterableDhis2Response iterableDhis2Response = getOperation.withoutPaging().transfer();
                if ( itemType == null )
                {
                    return (Iterator<T>) iterableDhis2Response
                        .returnAs( Map.class, path )
                        .iterator();
                }
                else
                {
                    return (Iterator<T>) iterableDhis2Response
                        .returnAs( Class.forName( itemType ), path )
                        .iterator();
                }
            }
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

}

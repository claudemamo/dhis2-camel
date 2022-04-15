package org.hisp.dhis.integration.camel.api;

import org.hisp.dhis.api.v2_37_4.model.ImportOptions;
import org.hisp.dhis.api.v2_37_4.model.JobConfigurationWebMessageResponse;
import org.hisp.dhis.api.v2_37_4.model.Notification;
import org.hisp.dhis.api.v2_37_4.model.WebMessage;
import org.hisp.dhis.integration.sdk.Dhis2Client;
import org.hisp.dhis.integration.sdk.api.operation.PutOperation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Dhis2ResourceTables
{
    private final Dhis2Client dhis2Client;

    public Dhis2ResourceTables( Dhis2Client dhis2Client )
    {
        this.dhis2Client = dhis2Client;
    }

    public void analytics( Boolean skipAggregate, Boolean skipEvents, Integer lastYears )
    {
        PutOperation putOperation = dhis2Client.put( "resourceTables/analytics" );
        if ( skipEvents != null )
        {
            putOperation.withParameter( "skipEvents", String.valueOf( skipEvents ) );
        }
        if ( skipEvents != null )
        {
            putOperation.withParameter( "skipAggregate", String.valueOf( skipAggregate ) );
        }
        if ( lastYears != null )
        {
            putOperation.withParameter( "lastYears", String.valueOf( lastYears ) );
        }

        String taskId = putOperation.transfer().returnAs( WebMessage.class ).getResponse().get().get( "id" );

        Notification notification = null;
        while ( notification == null || !notification.getCompleted().get() )
        {
            try
            {
                Thread.sleep( 30000 );
            }
            catch ( InterruptedException e )
            {
                throw new RuntimeException( e );
            }
            Iterable<Notification> notifications = dhis2Client.get( "system/tasks/ANALYTICS_TABLE/{taskId}",
                taskId ).withoutPaging().transfer().returnAs( Notification.class );
            if ( notifications.iterator().hasNext() )
            {
                notification = notifications.iterator().next();
                if ( notification.getLevel().get().equals( ImportOptions.NotificationLevel.ERROR ) )
                {
                    throw new RuntimeException( "Analytics failed => " + notification );
                }
            }
        }
    }

}

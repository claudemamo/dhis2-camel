package org.hisp.dhis.integration.camel;

import org.apache.camel.Processor;
import org.apache.camel.support.component.AbstractApiConsumer;

import org.hisp.dhis.integration.camel.internal.Dhis2ApiName;

public class Dhis2Consumer extends AbstractApiConsumer<Dhis2ApiName, Dhis2Configuration> {

    public Dhis2Consumer(Dhis2Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

}

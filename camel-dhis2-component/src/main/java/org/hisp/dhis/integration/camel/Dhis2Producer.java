package org.hisp.dhis.integration.camel;

import org.apache.camel.support.component.AbstractApiProducer;

import org.hisp.dhis.integration.camel.internal.Dhis2ApiName;
import org.hisp.dhis.integration.camel.internal.Dhis2PropertiesHelper;

public class Dhis2Producer extends AbstractApiProducer<Dhis2ApiName, Dhis2Configuration> {

    public Dhis2Producer(Dhis2Endpoint endpoint) {
        super(endpoint, Dhis2PropertiesHelper.getHelper(endpoint.getCamelContext()));
    }
}

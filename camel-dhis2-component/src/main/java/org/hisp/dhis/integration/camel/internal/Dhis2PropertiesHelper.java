package org.hisp.dhis.integration.camel.internal;

import org.apache.camel.CamelContext;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import org.hisp.dhis.integration.camel.Dhis2Configuration;

/**
 * Singleton {@link ApiMethodPropertiesHelper} for Dhis2 component.
 */
public final class Dhis2PropertiesHelper extends ApiMethodPropertiesHelper<Dhis2Configuration> {

    private static Dhis2PropertiesHelper helper;

    private Dhis2PropertiesHelper(CamelContext context) {
        super(context, Dhis2Configuration.class, Dhis2Constants.PROPERTY_PREFIX);
    }

    public static synchronized Dhis2PropertiesHelper getHelper(CamelContext context) {
        if (helper == null) {
            helper = new Dhis2PropertiesHelper(context);
        }
        return helper;
    }
}

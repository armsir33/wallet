package com.gears.leo.infrastructure;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import org.apache.commons.lang3.Validate;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.gears.leo.api.ApiService;
import com.gears.leo.domain.Service;
import com.gears.leo.domain.Shutdownable;

public class ServiceHelper {

    final static Logger LOG = LoggerFactory.getLogger(ServiceHelper.class);

    public static void addShutdownHook(final Shutdownable service) {
        Validate.notNull(service, "Valid shutdownable required");

        final Thread shutdownHook = new Thread() {
            @Override
            public void run() {
                try {
                    service.shutdown();
                } catch (final Exception e) {
                    LOG.warn("Error occurs when shuting down service", e);
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public Service createRestService(final String endpointAddress, final ApiService... restServices) {
        notBlank(endpointAddress, "Required, valid endpoint address");
        notNull(restServices, "Required, valid service");

        final JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();

        serverFactory.setProvider(this.createJacksonProvider());
        serverFactory.setAddress(endpointAddress);

        this.setResources(serverFactory, restServices);

        final Server server = serverFactory.create();
        LOG.info("REST service(s) STARTED at {}", endpointAddress);

        return () -> {
            server.stop();
            server.destroy();
            LOG.info("REST service(s) STOPPED");
        };
    }

    private JacksonJaxbJsonProvider createJacksonProvider() {
        LOG.debug("createJacksonProvider()");
        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL));
        jacksonJaxbJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return jacksonJaxbJsonProvider;
    }

    private void setResources(final JAXRSServerFactoryBean serverFactory, final ApiService... restServices) {
        LOG.debug("setResources()");
        for (final ApiService service : restServices) {
            serverFactory.setResourceClasses(service.getClass());
            serverFactory.setResourceProvider(service.getClass(), new SingletonResourceProvider(service));
            LOG.info("Service to start: {}", service.getClass().getCanonicalName());
        }
    }

}

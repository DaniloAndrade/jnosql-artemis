/*
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.jnosql.artemis.key;

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.configuration.ConfigurationReader;
import org.jnosql.artemis.configuration.ConfigurationSettingsUnit;
import org.jnosql.artemis.reflection.Reflections;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.BucketManagerFactory;
import org.jnosql.diana.api.key.KeyValueConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * The class that creates {@link BucketManagerFactory} from the {@link ConfigurationUnit}
 */
@ApplicationScoped
class BucketManagerFactoryProducer {


    @Inject
    private Reflections reflections;

    @Inject
    private ConfigurationReader configurationReader;

    @ConfigurationUnit
    @Produces
    public <T extends BucketManager> BucketManagerFactory<T> getBucketManagerGenerics(InjectionPoint injectionPoint) {
        return getBuckerManagerFactocy(injectionPoint);
    }

    @ConfigurationUnit
    @Produces
    public BucketManagerFactory getBucketManager(InjectionPoint injectionPoint) {
        return getBuckerManagerFactocy(injectionPoint);
    }

    private <T extends BucketManager> BucketManagerFactory<T> getBuckerManagerFactocy(InjectionPoint injectionPoint) {
        Annotated annotated = injectionPoint.getAnnotated();
        ConfigurationUnit annotation = annotated.getAnnotation(ConfigurationUnit.class);

        ConfigurationSettingsUnit unit = configurationReader.read(annotation, KeyValueConfiguration.class);
        Class<KeyValueConfiguration> configurationClass = unit.<KeyValueConfiguration>getProvider()
                .orElseThrow(() -> new IllegalStateException("The ColumnConfiguration provider is required in the configuration"));

        KeyValueConfiguration columnConfiguration = reflections.newInstance(configurationClass);
        BucketManagerFactory factory = columnConfiguration.get(unit.getSettings());

        return factory;
    }
}

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
package org.jnosql.artemis.document;


import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import java.util.Objects;

/**
 * The default implementation of {@link DocumentTemplateAsyncProducer}
 */
class DefaultDocumentTemplateAsyncProducer implements DocumentTemplateAsyncProducer {


    @Inject
    private DocumentEntityConverter converter;

    @Override
    public DocumentTemplateAsync get(DocumentCollectionManagerAsync collectionManager) throws NullPointerException {
        Objects.requireNonNull(collectionManager, "collectionManager is required");
        return new ProducerAbstractDocumentTemplateAsync(converter, collectionManager);
    }

    @Vetoed
    static class ProducerAbstractDocumentTemplateAsync extends AbstractDocumentTemplateAsync {

        private DocumentEntityConverter converter;

        private DocumentCollectionManagerAsync manager;

        ProducerAbstractDocumentTemplateAsync(DocumentEntityConverter converter, DocumentCollectionManagerAsync manager) {
            this.converter = converter;
            this.manager = manager;
        }

        ProducerAbstractDocumentTemplateAsync() {
        }

        @Override
        protected DocumentEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected DocumentCollectionManagerAsync getManager() {
            return manager;
        }
    }
}

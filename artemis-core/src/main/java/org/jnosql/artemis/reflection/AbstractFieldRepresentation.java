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
package org.jnosql.artemis.reflection;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jnosql.artemis.AttributeConverter;
import org.jnosql.diana.api.Value;

/**
 * Base class to all {@link FieldRepresentation}
 *
 * @see FieldRepresentation
 */
abstract class AbstractFieldRepresentation implements FieldRepresentation {

    protected final FieldType type;

    protected final Field field;

    protected final String name;

    protected final String fieldName;

    protected final Optional<Class<? extends AttributeConverter>> converter;

    AbstractFieldRepresentation(FieldType type, Field field, String name, Class<? extends AttributeConverter> converter) {
        this.type = type;
        this.field = field;
        this.name = name;
        this.fieldName = field.getName();
        this.converter = Optional.ofNullable(converter);
    }

    @Override
    public FieldType getType() {
        return type;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }


    @Override
    public <T extends AttributeConverter> Optional<Class<? extends AttributeConverter>> getConverter() {
        return converter;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("field", field)
                .append("name", name)
                .append("fieldName", fieldName)
                .toString();
    }

    public Object getValue(Value value) {
        return value.get(field.getType());
    }
}

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
package org.jnosql.artemis.column;


import org.jnosql.diana.api.NonUniqueResultException;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnQuery;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This interface that represents the common operation between an entity
 * and {@link org.jnosql.diana.api.column.ColumnEntity}
 *
 * @see org.jnosql.diana.api.column.ColumnFamilyManager
 */
public interface ColumnTemplate {

    /**
     * Inserts entity
     *
     * @param entity entity to be saved
     * @param <T>    the instance type
     * @return the entity saved
     * @throws NullPointerException when document is null
     */
    <T> T insert(T entity) throws NullPointerException;


    /**
     * Inserts entity with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @param <T>    the instance type
     * @return the entity saved
     */
    <T> T insert(T entity, Duration ttl);


    /**
     * Inserts entity, by default it's just run for each saving using
     * {@link ColumnTemplate#insert(Object)}},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default <T> Iterable<T> insert(Iterable<T> entities) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::insert).collect(Collectors.toList());
    }

    /**
     * Inserts documents collection entity with time to live, by default it's just run for each saving using
     * {@link ColumnTemplate#insert(Object, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default <T> Iterable<T> insert(Iterable<T> entities, Duration ttl) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(d -> insert(d, ttl)).collect(Collectors.toList());
    }


    /**
     * Updates a entity
     *
     * @param entity entity to be updated
     * @param <T>    the instance type
     * @return the entity updated
     * @throws NullPointerException when entity is null
     */
    <T> T update(T entity) throws NullPointerException;


    /**
     * Saves entity, by default it's just run for each saving using
     * {@link ColumnTemplate#update(Object)}},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be updated
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default <T> Iterable<T> update(Iterable<T> entities) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::update).collect(Collectors.toList());
    }

    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException when query is null
     */
    void delete(ColumnDeleteQuery query) throws NullPointerException;


    /**
     * Finds entities from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> List<T> select(ColumnQuery query) throws NullPointerException;

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when query is null
     */
    default <T> Optional<T> singleResult(ColumnQuery query) throws NonUniqueResultException {
        List<T> entities = select(query);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The query returns more than one entity, query: " + query);
    }

}

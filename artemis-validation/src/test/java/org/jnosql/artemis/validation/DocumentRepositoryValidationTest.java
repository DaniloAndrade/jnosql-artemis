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
package org.jnosql.artemis.validation;

import org.jnosql.artemis.document.DocumentTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Set;

import static java.util.Collections.singletonList;

@RunWith(WeldJUnit4Runner.class)
public class DocumentRepositoryValidationTest {

    @Inject
    private DocumentTemplate repository;


    @Test
    public void shouldValidateOnSave() {

        Person person = Person.builder()
                .withAge(21)
                .withName("Ada")
                .withSalary(BigDecimal.ONE)
                .withPhones(singletonList("123131231"))
                .build();
        repository.insert(person);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldReturnValidationExceptionOnSave() {
        Person person = Person.builder()
                .withAge(10)
                .withName("Ada")
                .withSalary(BigDecimal.ONE)
                .withPhones(singletonList("123131231"))
                .build();
        repository.insert(person);
    }


    @Test
    public void shouldGetValidationsOnSave() {

        Person person = Person.builder()
                .withAge(10)
                .withName("Ada")
                .withSalary(BigDecimal.valueOf(12991))
                .withPhones(singletonList("123131231"))
                .build();
        try {
            repository.insert(person);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            Assert.assertEquals(2, violations.size());
        }

    }

    @Test
    public void shouldValidateOnUpdate() {

        Person person = Person.builder()
                .withAge(21)
                .withName("Ada")
                .withSalary(BigDecimal.ONE)
                .withPhones(singletonList("123131231"))
                .build();
        repository.update(person);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldReturnValidationExceptionOnUpdate() {
        Person person = Person.builder()
                .withAge(10)
                .withName("Ada")
                .withSalary(BigDecimal.ONE)
                .withPhones(singletonList("123131231"))
                .build();
        repository.update(person);
    }


    @Test
    public void shouldGetValidationsOnUpdate() {

        Person person = Person.builder()
                .withAge(10)
                .withName("Ada")
                .withSalary(BigDecimal.valueOf(12991))
                .withPhones(singletonList("123131231"))
                .build();
        try {
            repository.update(person);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            Assert.assertEquals(2, violations.size());
        }

    }
}

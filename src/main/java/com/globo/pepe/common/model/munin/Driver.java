/*
 * Copyright (c) 2019. Globo.com - ATeam
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Authors: See AUTHORS file
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globo.pepe.common.model.munin;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;

@Entity
public class Driver extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    public enum Type {
        NOOP,
        JDBC,
        HTTP
    }

    private String jar;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "driver")
    private Set<Connection> connections;

    public Driver() {
        super();
    }

    public Driver(String name, Type type) {
        super(name);
        Assert.notNull(type, "Type must not be null!");

        this.type = type;
    }

    public String getJar() {
        return jar;
    }

    public Driver setJar(String jar) {
        this.jar = jar;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Driver setType(Type type) {
        Assert.notNull(type, "Type must not be null!");

        this.type = type;
        return this;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public Driver setConnections(Set<Connection> connections) {
        this.connections = connections;
        return this;
    }
}

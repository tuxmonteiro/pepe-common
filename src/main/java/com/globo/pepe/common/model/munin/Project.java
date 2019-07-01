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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Project extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Embedded
    private Keystone keystone;

    @OneToMany(mappedBy = "project")
    private Set<Metric> metrics = new HashSet<>();

    public Project() {
        super();
    }

    public Project(String name) {
        super(name);
    }

    public Keystone getKeystone() {
        return keystone;
    }

    public Project setKeystone(Keystone keystone) {
        this.keystone = keystone;
        return this;
    }

    public Set<Metric> getMetrics() {
        return metrics;
    }

    public Project setMetrics(Set<Metric> metrics) {
        if (metrics != null) {
            this.metrics = metrics;
        }
        return this;
    }
}

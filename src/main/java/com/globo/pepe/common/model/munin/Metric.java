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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.util.Assert;

@Entity
@JsonIgnoreProperties(value = {"last_processing"}, allowGetters = true)
public class Metric extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String query;

    @ManyToOne
    @JoinColumn(name = "connection_id", nullable = false, foreignKey = @ForeignKey(name="FK_metric_connection"))
    private Connection connection;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name="FK_metric_project"))
    private Project project;

    @Column(name = "trigger_name", nullable = false, unique = true)
    private String trigger;

    @Column(name = "last_processing")
    @JsonProperty("last_processing")
    private Date lastProcessing = new Date(0);

    @Column(name = "interval_time")
    @JsonProperty("interval")
    private Long interval = 30L;

    @Column(name = "metric_enable")
    private Boolean enable = true;

    public Metric() {
        super();
    }

    public Metric(String name, String query, Connection connection, Project project) {
        super(name);
        Assert.hasText(query, "Query must not be null or empty!");
        Assert.notNull(connection, "Connection must not be null!");
        Assert.notNull(project, "Project must not be null!");

        this.query = query;
        this.connection = connection;
        this.project = project;
    }

    public String getQuery() {
        return query;
    }

    public Metric setQuery(String query) {
        this.query = query;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }

    public Metric setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public Metric setProject(Project project) {
        this.project = project;
        return this;
    }

    public String getTrigger() {
        return trigger;
    }

    public Metric setTrigger(String trigger) {
        this.trigger = trigger;
        return this;
    }

    public Date getLastProcessing() {
        return lastProcessing;
    }

    public Metric setLastProcessing(Date lastProcessing) {
        this.lastProcessing = lastProcessing;
        return this;
    }

    public Long getInterval() {
        return interval;
    }

    public Metric setInterval(Long interval) {
        this.interval = interval;
        return this;
    }

    public Boolean isEnable() {
        return enable;
    }

    public Metric setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }
}

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.Assert;

@Entity
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class Connection extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String url;

    @Column
    private String login;

    @Column
    private String password;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false, foreignKey = @ForeignKey(name="FK_connection_driver"))
    private Driver driver;

    public Connection() {
        super();
    }

    public Connection(String name, String url, Driver driver) {
        super(name);
        Assert.hasText(url, "Url must not be null or empty!");
        Assert.notNull(driver, "Driver must not be null!");

        this.url = url;
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public Connection setUrl(String url) {
        Assert.hasText(url, "Url must not be null or empty!");
        this.url = url;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Connection setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Connection setPassword(String password) {
        this.password = password;
        return this;
    }

    public Driver getDriver() {
        return driver;
    }

    public Connection setDriver(Driver driver) {
        Assert.notNull(driver, "Driver must not be null!");

        this.driver = driver;
        return this;
    }
}

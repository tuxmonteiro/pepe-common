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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.globo.pepe.common.annotation.JsonCustomProperties;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

@MappedSuperclass
@JsonCustomProperties
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Persistable<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    @JsonProperty("_created_by")
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonProperty("_created_at")
    private Date createdAt;

    @LastModifiedBy
    @Column(nullable = false)
    @JsonProperty("_last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonProperty("_last_modified_at")
    private Date lastModifiedAt;

    public AbstractEntity() {
    }

    public AbstractEntity(String name) {
        Assert.hasText(name, "Name must not be null or empty!");

        this.name = name;
    }

    @Nullable
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AbstractEntity setName(String name) {
        Assert.hasText(name, "Name must not be null or empty!");

        this.name = name;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    @Transient
    @JsonIgnore
    public boolean isNew() {
        return null == this.getId();
    }

    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        } else {
            AbstractPersistable<?> that = (AbstractPersistable)obj;
            return null != id && id.equals(that.getId());
        }
    }

    public int hashCode() {
        return (null == id ? 0 : id.hashCode());
    }
}

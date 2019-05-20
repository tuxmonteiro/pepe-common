/*
 * Copyright (c) 2017-2019 Globo.com
 * All rights reserved.
 *
 * This source is subject to the Apache License, Version 2.0.
 * Please see the LICENSE file for more information.
 *
 * Authors: See AUTHORS file
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globo.pepe.common.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class Event {

    private static final long serialVersionUID = 1L;

    static final JsonNode EMPTY_JSON = JsonNodeFactory.instance.objectNode();

    private String id = "";

    private Metadata metadata = new Metadata();

    private JsonNode payload = EMPTY_JSON;

    public String getId() {
        return id;
    }

    public Event setId(String id) {
        this.id = id != null ? id : "";
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Event setMetadata(Metadata metadata) {
        this.metadata = metadata != null ? metadata : new Metadata();
        return this;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public Event setPayload(JsonNode payload) {
        this.payload = payload != null ? payload : EMPTY_JSON;
        return this;
    }
}

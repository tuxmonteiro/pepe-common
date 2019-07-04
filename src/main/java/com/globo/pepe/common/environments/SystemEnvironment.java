/*
 * Copyright (c) 2014-2018 Globo.com - ATeam
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

package com.globo.pepe.common.environments;

import java.util.Optional;

/**
 * The enum System environments.
 */
@SuppressWarnings("unused")
public enum SystemEnvironment {
    // @formatter:off

    // DATABASE

    /**
     * The property controls the maximum size that the pool is allowed to reach, including both idle and in-use
     * connections. Basically this value will determine the maximum number of actual connections to the database
     * backend.
     *
     * When the pool reaches this size, and no idle connections are available, calls to getConnection() will
     * block for up to connectionTimeout milliseconds before timing out.
     */
    DB_MAX_POOL_SIZE              ("DB_MAX_POOL_SIZE",              20),

    /**
     * Set the maximum number of milliseconds that a client will wait for a connection from the pool. If this
     * time is exceeded without a connection becoming available, a SQLException will be thrown from
     */
    DB_CONN_TIMEOUT               ("DB_CONN_TIMEOUT",               0L),

    /**
     * The property controls the maximum lifetime of a connection in the pool. An in-use connection will never
     * be retired, only when it is closed will it then be removed. On a connection-by-connection basis, minor
     * negative attenuation is applied to avoid mass-extinction in the pool.
     */
    DB_MAX_LIFE_TIME               ("DB_MAX_LIFE_TIME",             1800000),

    /**
     * Set the default auto-commit behavior of connections in the pool.
     */
    DB_AUTOCOMMIT                 ("DB_AUTOCOMMIT",                 Boolean.FALSE),

    /**
     * Enable Cache prepared statements
     */
    DB_CACHE_PREP_STMTS           ("DB_CACHE_PREP_STMTS",           Boolean.TRUE),

    /**
     * This sets the number of prepared statements that the MySQL driver will cache per connection.
     * The default is a conservative 25. We recommend setting this to between 250-500.
     */
    DB_PREP_STMT_CACHE_SIZE       ("DB_PREP_STMT_CACHE_SIZE",       500),

    /**
     * Newer versions of MySQL support server-side prepared statements,
     * this can provide a substantial performance boost
     */
    DB_USE_SERVER_PREP_STMTS      ("DB_USE_SERVER_PREP_STMTS",      Boolean.TRUE),

    /**
     * This is the maximum length of a prepared SQL statement that the driver will cache.
     * The MySQL default is 256. In our experience, especially with ORM frameworks like Hibernate,
     * this default is well below the threshold of generated statement lengths. Our recommended setting is 2048.
     */
    DB_PREP_STMT_CACHE_SQL_LIMIT  ("DB_PREP_STMT_CACHE_SQL_LIMIT",  2048),

    /**
     * Should the driver refer to the internal values of autocommit and transaction isolation that are set by
     * Connection.setAutoCommit() and Connection.setTransactionIsolation() and transaction state as maintained by
     * the protocol, rather than querying the database or blindly sending commands to the database for commit()
     * or rollback() method calls?
     */
    DB_USE_LOCAL_SESSION_STATE    ("DB_USE_LOCAL_SESSION_STATE",    Boolean.TRUE),

    /**
     * Should the driver use multiqueries (irregardless of the setting of "allowMultiQueries") as well as
     * rewriting of prepared statements for INSERT into multi-value inserts when executeBatch() is called?
     */
    DB_REWRITE_BATCHED_STATEMENTS ("DB_REWRITE_BATCHED_STATEMENTS", Boolean.TRUE),

    /**
     *  Should the driver cache ResultSetMetaData for Statements and PreparedStatements?
     */
    DB_CACHE_RESULT_SET_METADATA  ("DB_CACHE_RESULT_SET_METADATA",  Boolean.TRUE),

    /**
     * Should the driver cache the results of 'SHOW VARIABLES' and 'SHOW COLLATION' on a per-URL basis?
     */
    DB_CACHE_SERVER_CONFIGURATION ("DB_CACHE_SERVER_CONFIGURATION", Boolean.TRUE),

    /**
     * If using MySQL-4.1 or newer, should the driver only issue 'set autocommit=n' queries
     * when the server's state doesn't match the requested state by Connection.setAutoCommit(boolean)?
     */
    DB_ELIDE_SET_AUTO_COMMITS     ("DB_ELIDE_SET_AUTO_COMMITS",     Boolean.TRUE),

    /**
     * Should the driver maintain various internal timers to enable idle time calculations as well as more
     * verbose error messages when the connection to the server fails?
     * Setting this property to false removes at least two calls to System.getCurrentTimeMillis() per query.
     */
    DB_MAINTAIN_TIME_STATS        ("DB_MAINTAIN_TIME_STATS",        Boolean.FALSE);

    // @formatter:on

    /**
     * Gets SystemEnv value.
     *
     * @return the enum value
     */
    public String getValue() {
        return value;
    }

    private final String value;

    SystemEnvironment(String env, Object def) {
        this.value = Optional.ofNullable(System.getenv(env)).orElse(String.valueOf(def));
    }
}

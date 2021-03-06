/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.mapping;

import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.util.Assert;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.UserType;

/**
 * Default implementation of {@link UserTypeResolver} that resolves {@link UserType} by their name from
 * {@link Cluster#getMetadata()}.
 *
 * @author Mark Paluch
 * @since 1.5
 */
public class SimpleUserTypeResolver implements UserTypeResolver {

	private final String keyspaceName;
	private final Cluster cluster;

	/**
	 * Create a new {@link SimpleUserTypeResolver}.
	 *
	 * @param cluster must not be {@literal null}.
	 * @param keyspaceName must not be empty or {@literal null}.
	 */
	public SimpleUserTypeResolver(Cluster cluster, String keyspaceName) {

		Assert.notNull(cluster, "Cluster must not be null");
		Assert.hasText(keyspaceName, "Keyspace must not be null or empty");

		this.keyspaceName = keyspaceName;
		this.cluster = cluster;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.cassandra.mapping.UserTypeResolver#resolveType(org.springframework.cassandra.core.cql.CqlIdentifier)
	 */
	@Override
	public UserType resolveType(CqlIdentifier typeName) {

		KeyspaceMetadata keyspace = cluster.getMetadata().getKeyspace(keyspaceName);

		return keyspace.getUserType(typeName.toCql());
	}
}

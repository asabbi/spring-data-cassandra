/*
 * Copyright 2014-2016 the original author or authors.
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
package org.springframework.data.cassandra.repository.query;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.repository.query.QueryCreationException;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * {@link RepositoryQuery} implementation for Cassandra.
 *
 * @author Matthew Adams
 * @author Mark Paluch
 */
public class PartTreeCassandraQuery extends AbstractCassandraQuery {

	private final CassandraMappingContext mappingContext;

	private final PartTree tree;

	/**
	 * Create a new {@link PartTreeCassandraQuery} from the given {@link QueryMethod} and {@link CassandraTemplate}.
	 *
	 * @param queryMethod must not be {@literal null}.
	 * @param operations must not be {@literal null}.
	 */
	public PartTreeCassandraQuery(CassandraQueryMethod queryMethod, CassandraOperations operations) {

		super(queryMethod, operations);

		try {
			this.tree = new PartTree(queryMethod.getName(), queryMethod.getEntityInformation().getJavaType());
			this.mappingContext = operations.getConverter().getMappingContext();
		} catch (Exception e) {
			throw QueryCreationException.create(queryMethod, e);
		}
	}

	/**
	 * Return the {@link PartTree} backing the query.
	 *
	 * @return the tree
	 */
	public PartTree getTree() {
		return tree;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.repository.query.AbstractCassandraQuery#createQuery(org.springframework.data.cassandra.repository.query.CassandraParameterAccessor, boolean)
	 */
	@Override
	protected String createQuery(CassandraParameterAccessor parameterAccessor) {

		CassandraQueryCreator queryCreator = new CassandraQueryCreator(tree, parameterAccessor, mappingContext,
				getQueryMethod().getEntityInformation());

		return queryCreator.createQuery().toString();
	}
}

package org.appenders.log4j2.elasticsearch.bulkprocessor.smoke;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2018 Rafal Foltynski
 * %%
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
 * #L%
 */


import org.appenders.log4j2.elasticsearch.AsyncBatchDelivery;
import org.appenders.log4j2.elasticsearch.BatchDelivery;
import org.appenders.log4j2.elasticsearch.ElasticsearchAppender;
import org.appenders.log4j2.elasticsearch.NoopIndexNameFormatter;
import org.appenders.log4j2.elasticsearch.bulkprocessor.BasicCredentials;
import org.appenders.log4j2.elasticsearch.bulkprocessor.BulkProcessorObjectFactory;
import org.appenders.log4j2.elasticsearch.bulkprocessor.PEMCertInfo;
import org.appenders.log4j2.elasticsearch.bulkprocessor.XPackAuth;
import org.appenders.log4j2.elasticsearch.smoke.SmokeTestBase;
import org.junit.Ignore;

@Ignore
public class SmokeTest extends SmokeTestBase {

    @Override
    public ElasticsearchAppender.Builder createElasticsearchAppenderBuilder(boolean messageOnly, boolean buffered, boolean secured) {

        BulkProcessorObjectFactory.Builder builder = BulkProcessorObjectFactory.newBuilder()
                .withServerUris("tcp://localhost:9300");

        if (secured) {
            PEMCertInfo certInfo = PEMCertInfo.newBuilder()
                    .withKeyPath(System.getProperty("pemCertInfo.keyPath"))
                    .withKeyPassphrase(System.getProperty("pemCertInfo.keyPassphrase"))
                    .withClientCertPath(System.getProperty("pemCertInfo.clientCertPath"))
                    .withCaPath(System.getProperty("pemCertInfo.caPath"))
                    .build();

            BasicCredentials credentials = BasicCredentials.newBuilder()
                    .withUsername("admin")
                    .withPassword("changeme")
                    .build();

            XPackAuth auth = XPackAuth.newBuilder()
                    .withCertInfo(certInfo)
                    .withCredentials(credentials)
                    .build();

            builder.withAuth(auth);

        }

        BulkProcessorObjectFactory bulkProcessorObjectFactory = builder.build();

        BatchDelivery asyncBatchDelivery = AsyncBatchDelivery.newBuilder()
                .withClientObjectFactory(bulkProcessorObjectFactory)
                .withBatchSize(30000)
                .withDeliveryInterval(1000)
                .build();

        NoopIndexNameFormatter indexNameFormatter = NoopIndexNameFormatter.newBuilder()
                .withIndexName("log4j2_test_es5")
                .build();

        return ElasticsearchAppender.newBuilder()
                .withName("elasticsearch")
                .withBatchDelivery(asyncBatchDelivery)
                .withIndexNameFormatter(indexNameFormatter)
                .withIgnoreExceptions(false);
    }

}

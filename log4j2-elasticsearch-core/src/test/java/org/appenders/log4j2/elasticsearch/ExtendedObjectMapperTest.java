package org.appenders.log4j2.elasticsearch;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2019 Rafal Foltynski
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtendedObjectMapperTest {

    @Test
    public void newWriterReturnsExtendedObjectWrite() {

        // given
        ExtendedObjectMapper mapper = new ExtendedObjectMapper(new JsonFactory());

        // when
        ObjectWriter writer = mapper._newWriter(mapper.getSerializationConfig(), mapper.getTypeFactory().constructArrayType(Object.class), null);

        // then
        assertEquals(ExtendedObjectWriter.class, writer.getClass());
    }

}

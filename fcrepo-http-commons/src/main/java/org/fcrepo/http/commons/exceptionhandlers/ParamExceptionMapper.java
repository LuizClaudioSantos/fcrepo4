/*
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.http.commons.exceptionhandlers;

import org.glassfish.jersey.server.ParamException;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.fromResponse;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Handle Jersey ParamException
 *
 * @author awoods
 * @since 2015-01-20
 */
@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

    private static final Logger LOGGER = getLogger(ParamExceptionMapper.class);

    @Override
    public Response toResponse(final ParamException e) {

        LOGGER.error("ParamException intercepted by ParamExceptionMapper: {}\n", e.getMessage());

        final StringBuilder msg = new StringBuilder("Error parsing parameter: ");
        msg.append(e.getParameterName());
        msg.append(", of type: ");
        msg.append(e.getParameterType().getSimpleName());

        return fromResponse(e.getResponse()).entity(msg.toString()).build();
    }

}

/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.core.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdviceFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAdviceFilter.class);
    
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }
   
    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
        chain.doFilter(request, response);
    }
    
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    }

    protected void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
    }
    
    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Exception exception = null;

        try {

            boolean continueChain = preHandle(request, response);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Invoked preHandle method.  Continuing chain?: [" + continueChain + "]");
            }
            if (continueChain) {
                executeChain(request, response, chain);
            }
            postHandle(request, response);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Successfully invoked postHandle method");
            }

        } catch (Exception e) {
            exception = e;
        } finally {
            cleanup(request, response, exception);
        }
    }

  
    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws ServletException, IOException {
        Exception exception = existing;
        try {
            afterCompletion(request, response, exception);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Successfully invoked afterCompletion method.");
            }
        } catch (Exception e) {
            if (exception == null) {
                exception = e;
            } else {
                LOG.debug("afterCompletion implementation threw an exception.  This will be ignored to " +
                        "allow the original source exception to be propagated.", e);
            }
        }
        if (exception != null) {
            if (exception instanceof ServletException) {
                throw (ServletException) exception;
            } else if (exception instanceof IOException) {
                throw (IOException) exception;
            } else {
                if (LOG.isDebugEnabled()) {
                    String msg = "Filter execution resulted in an unexpected Exception " +
                            "(not IOException or ServletException as the Filter API recommends).  " +
                            "Wrapping in ServletException and propagating.";
                    LOG.debug(msg);
                }
                throw new ServletException(exception);
            }
        }
    }

}

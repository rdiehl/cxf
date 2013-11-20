/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.validation;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.cxf.common.util.PropertyUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class ValidationOutInterceptor extends AbstractValidationInterceptor {
    private boolean enforceOnlyBeanConstraints;
    public ValidationOutInterceptor() {
        super(Phase.PRE_MARSHAL);
    }
    public ValidationOutInterceptor(String phase) {
        super(phase);
    }
    
    @Override
    protected void handleValidation(final Message message, final Object resourceInstance,
                                    final Method method, final List<Object> arguments) {
        if (arguments.size() == 1
            && !PropertyUtils.isTrue(message.getExchange().get(ValidationInInterceptor.INPUT_VALIDATION_FAILED))) {
            Object entity = unwrapEntity(arguments.get(0));
            ValidationProvider theProvider = getOutProvider(message);
            if (isEnforceOnlyBeanConstraints()) {
                theProvider.validateReturnValue(entity);    
            } else {
                theProvider.validateReturnValue(resourceInstance, method, entity);
            }
        }        
    }
    
    protected Object unwrapEntity(Object entity) {
        return entity;
    }
    
    protected ValidationProvider getOutProvider(Message message) {
        ValidationProvider provider = message.getExchange().get(ValidationProvider.class);
        return provider == null ? getProvider(message) : provider;
    }
    
    public boolean isEnforceOnlyBeanConstraints() {
        return enforceOnlyBeanConstraints;
    }
    public void setEnforceOnlyBeanConstraints(boolean enforceOnlyBeanConstraints) {
        this.enforceOnlyBeanConstraints = enforceOnlyBeanConstraints;
    }
        
    
}

/*
 * Copyright 2010, 2011  Service de logiciel et developpement R.J. Potter (Robert J Potter)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.sldrjp.wonderland.modules.cardwall.common;

import java.util.HashMap;
import java.util.Map;

public class MethodCalled {

    String methodName;
    int numberOfTimesCalled = 0;

    static Map methods = new HashMap<String, MethodCalled>();

    public static void reset() {
       methods = new HashMap<String, MethodCalled>();
    }
    public static void called(String methodName) {
        MethodCalled method = (MethodCalled) methods.get(methodName);
        if (method == null) {
            method = new MethodCalled(methodName);
            methods.put(methodName, method);
        }
        method.called();
    }

    public MethodCalled(String methodName) {
        this.methodName = methodName;
    }

    public int getNumberOfTimesCalled() {
        return numberOfTimesCalled;
    }

    public String getMethodName() {

        return methodName;
    }

    private void called() {
        numberOfTimesCalled++;
    }

    public static boolean wasCalled(String methodName) {
       MethodCalled method = (MethodCalled) methods.get(methodName);
        if (method != null) {
            return method.getNumberOfTimesCalled() > 0;
        }
        return false;
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hw.lineage.loader.utils;

/**
 * @description: A collection of utility functions for dealing with exceptions and exception workflows.
 * @author: HamaWhite
 */
public class ExceptionUtils {

    private ExceptionUtils() {
    }

    /**
     * Throws the given {@code Throwable} in scenarios where the signatures do not allow you to
     * throw an arbitrary Throwable. Errors and RuntimeExceptions are thrown directly, other
     * exceptions are packed into runtime exceptions
     *
     * @param t The throwable to be thrown.
     */
    public static void rethrow(Throwable t) {
        if (t instanceof Error) {
            throw (Error) t;
        } else if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new RuntimeException(t);
        }
    }
}

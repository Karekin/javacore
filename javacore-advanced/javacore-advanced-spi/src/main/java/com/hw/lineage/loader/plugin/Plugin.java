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

package com.hw.lineage.loader.plugin;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Interface for plugins. Plugins typically extend this interface in their SPI and the concrete
 * implementations of a service then implement the SPI contract.
 *
 * @description: Plugin
 * @author: HamaWhite
 */
public interface Plugin {

    /**
     * Helper method to get the class loader used to load the plugin. This may be needed for some
     * plugins that use dynamic class loading afterwards the plugin was loaded.
     *
     * @return the class loader used to load the plugin.
     */
    default ClassLoader getClassLoader() {
        return checkNotNull(
                this.getClass().getClassLoader(),
                "%s plugin with null class loader",
                this.getClass().getName());
    }
}

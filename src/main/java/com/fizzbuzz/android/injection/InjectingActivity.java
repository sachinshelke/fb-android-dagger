/*
 * Copyright (c) 2013 Fizz Buzz LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fizzbuzz.android.injection;

import android.app.Activity;
import dagger.ObjectGraph;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Manages an ObjectGraph on behalf of an Activity.  This graph is created by extending the application-scope graph with
 * Activity-specific module(s).
 */
public class InjectingActivity
        extends Activity
        implements Injector {
    private ObjectGraph mObjectGraph;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // extend the application-scope object graph with the modules for this activity
        mObjectGraph = ((Injector)getApplication()).getObjectGraph().plus(getModules().toArray());

        // now we can inject ourselves
        inject(this);
    }

    // implement Injector interface

    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }
    @Override
    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be assigned prior to calling inject");
        mObjectGraph.inject(target);
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<Object>();
        result.add(new InjectingActivityModule(this));
        return result;
    }
}
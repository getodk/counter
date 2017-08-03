/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opendatakit.counter.dto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Answer extends RealmObject {

    @PrimaryKey
    // id of an answer is represented by combination of formId and questionId
    // eg. bed-net-survey1 where formId = bed-net-survey and questionId = 1
    private String id;

    private int value;

    public Answer() {
        // no arguments constructor required for realm
    }

    public Answer(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}

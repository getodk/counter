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

package org.opendatakit.counter.dao;

import org.opendatakit.counter.dto.Answer;

import java.util.List;

import io.realm.Realm;

public class AnswerDao {

    public static void saveAnswer(Answer answer) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(answer);
        realm.commitTransaction();
    }

    public static Integer getValue(String id) {
        List<Answer> answers = getAnswers(id);
        return answers.size() > 0 ? answers.get(0).getValue() : null;
    }

    private static List<Answer> getAnswers(String id) {
        return Realm
                .getDefaultInstance()
                .where(Answer.class)
                .equalTo("id", id)
                .findAll();
    }
}

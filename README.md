# ODK Counter

This app is intended to be used from ODK Collect as a counter.

[Sample xml form](https://github.com/opendatakit/counter/blob/master/counter.xml)
[Sample xls form](https://github.com/opendatakit/counter/blob/master/counter.xlsx)

In order to use the app you need to create a form which contains an [ExIntegerWidget](https://github.com/opendatakit/collect/blob/master/collect_app/src/main/java/org/odk/collect/android/widgets/ExIntegerWidget.java) and add an appearance according to this pattern:

```java
appearance="ex:org.opendatakit.counter(form_id='counter-form', form_name='Counter Form', question_id='1', question_name='Counter 1', increment=true())"
```

**form_id** - a unique form identifier

**form_name** - a form name (it will be displayed for a user)

**question_id** - a unique question identifier

**question_name** - a question name (it will be displayed for a user)

**increment=true()** - an optional parameter. If set to true an initial value displayed in the Counter app is 0 and it will be increased by 1 after 0.5s otherwise the initial value is set to 1 and autoincrement doesn't take place.


form_id and question_id are needed since we store last saved values for pairs <form_id, question_id> to set the value of a counter when a user opens the app for the same pair <form_id, question_id> again.

You can always use translations in **form_name** and **question_name** as presented in the [sample form](https://github.com/opendatakit/counter/blob/master/counterForm.xml):
```java
appearance="ex:org.opendatakit.counter(form_id='counter-form', form_name=jr:itext('/data/form_name:label'), question_id='2', question_name=jr:itext('/data/Counter2:label'), increment=true())"
```

Selected value is returned to the ODK Collect via the RESULT_OK mechanism.

![Alt Text](https://github.com/opendatakit/counter/blob/master/counter.gif)


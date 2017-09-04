# ODK Counter

![Platform](https://img.shields.io/badge/platform-Android-blue.svg)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build status](https://circleci.com/gh/opendatakit/counter.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/opendatakit/counter)
[![Slack status](http://slack.opendatakit.org/badge.svg)](http://slack.opendatakit.org)

This app is intended to be used from ODK Collect as a counter.

* ODK website: [https://opendatakit.org](https://opendatakit.org)
* ODK forum: [https://forum.opendatakit.org](https://forum.opendatakit.org)
* ODK developer Slack chat: [http://slack.opendatakit.org](http://slack.opendatakit.org) 
* ODK developer Slack archive: [http://opendatakit.slackarchive.io](http://opendatakit.slackarchive.io) 
* ODK developer wiki: [https://github.com/opendatakit/opendatakit/wiki](https://github.com/opendatakit/opendatakit/wiki)

## Usage

In order to use the app you need to create a form which contains an [ExIntegerWidget](https://github.com/opendatakit/collect/blob/master/collect_app/src/main/java/org/odk/collect/android/widgets/ExIntegerWidget.java) and add an appearance according to this pattern:

```
appearance="ex:org.opendatakit.counter(form_id='counter-form', form_name='Counter Form', question_id='1', question_name='Counter 1', increment=true())"
```

**form_id** - a unique form identifier

**form_name** - a form name (displayed to the user)

**question_id** - a unique question identifier

**question_name** - a question name (displayed to the user)

**increment=true()** - an optional parameter. If set to true an initial value displayed in the Counter app is 0 and it will be increased by 1 after 0.5s otherwise the initial value is set to 1 and autoincrement doesn't take place.

You can use translations in **form_name** and **question_name** by following this pattern:
```
appearance="ex:org.opendatakit.counter(form_id='counter-form', form_name=jr:itext('/data/form_name:label'), question_id='2', question_name=jr:itext('/data/Counter2:label'), increment=true())"
```

`form_id` and `question_id` are needed since we store last saved values for pairs `<form_id, question_id>` to set the value of a counter when a user opens the app for the same pair `<form_id, question_id>` again. The selected value is returned to the ODK Collect via the RESULT_OK mechanism.

To get started, use the provided sample forms: [XLSForm (xlsx)](https://github.com/opendatakit/counter/blob/master/docs/counter.xlsx), [XForm (xml)](https://github.com/opendatakit/counter/blob/master/docs/counter.xml).

## Demo

![Alt Text](https://github.com/opendatakit/counter/blob/master/docs/counter.gif)


## Setting up your development environment

1. Download and install [Git](https://git-scm.com/downloads) and add it to your PATH

1. Download and install [Android Studio](https://developer.android.com/studio/index.html) 

1. Fork the counter project ([why and how to fork](https://help.github.com/articles/fork-a-repo/))

1. Clone your fork of the project locally. At the command line:

        git clone https://github.com/YOUR-GITHUB-USERNAME/counter

 If you prefer not to use the command line, you can use Android Studio to create a new project from version control using `https://github.com/YOUR-GITHUB-USERNAME/counter`. 

1. Open the project in the folder of your clone from Android Studio. To run the project, click on the green arrow at the top of the screen. The emulator is very slow so we generally recommend using a physical device when possible.

## Contributing code
Any and all contributions to the project are welcome.

Issues tagged as [quick win](https://github.com/opendatakit/counter/labels/quick%20win) should be a good place to start. There are also currently many issues tagged as [needs reproduction](https://github.com/opendatakit/counter/labels/needs%20reproduction) which need someone to try to reproduce them with the current version of ODK Counter and comment on the issue with their findings.

If you're ready to contribute code, see [the contribution guide](CONTRIBUTING.md).

## Contributing testing
All releases are verified on the following devices (ordered by Android version):
* [Samsung Galaxy Young GT-S6310](http://www.gsmarena.com/samsung_galaxy_young_s6310-5280.php) - Android 4.1.2
* [Infinix Race Bolt Q X451](http://bestmobs.com/infinix-race-bolt-q-x451) - Android 4.2.1
* [Samsung Galaxy J1 SM-J100H](http://www.gsmarena.com/samsung_galaxy_j1-6907.php) - Android 4.4.4
* [Huawei Y560-L01](http://www.gsmarena.com/huawei_y560-7829.php) - Android 5.1.1
* [Sony Xperia Z3 D6603](http://www.gsmarena.com/sony_xperia_z3-6539.php) -Android 6.0.1

Our regular code contributors use these devices (ordered by Android version): 
* [XOLO Q700s plus](http://www.gsmarena.com/xolo_q700s_plus-6624.php) - Android 4.4.2
* [Samsung Galaxy S4 GT-I9506](http://www.gsmarena.com/samsung_i9506_galaxy_s4-5542.php) - Android 5.0.1
* [Samsung Galaxy Tab SM-T285](http://www.gsmarena.com/samsung_galaxy_tab_a_7_0_(2016)-7880.php) - Android 5.1.1
* [Motorola G4 4th Gen XT1625](http://www.gsmarena.com/motorola_moto_g4-8103.php) - Android 7.0

## Downloading builds
Per-commit debug builds can be found on [CircleCI](https://circleci.com/gh/opendatakit/counter). Login with your GitHub account, click the build you'd like, then find the APK in the Artifacts tab.

## Creating signed releases for Google Play Store
Project maintainers have the keys to upload signed releases to the Play Store. 

Maintainers have a `secrets.properties` file in the `app` folder with the following:
```
// app/secrets.properties
RELEASE_STORE_FILE=/path/to/counter.keystore
RELEASE_STORE_PASSWORD=secure-store-password
RELEASE_KEY_ALIAS=key-alias
RELEASE_KEY_PASSWORD=secure-alias-password
```
To generate official signed releases, you'll need the keystore file, the keystore passwords, a configured `secrets.properties` file, and then run `./gradlew assembleRelease`. If successful, a signed release will be at `app/build/outputs/apk`.

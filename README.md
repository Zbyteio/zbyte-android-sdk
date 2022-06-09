<p align="center">
  <img src="https://user-images.githubusercontent.com/106575637/171570796-e2bedb8c-83ba-48ca-b3de-215d3300435f.png?raw=true" alt="zbyte"/>
</p>

# About ZByte Platform

The ZByte Platform helps you to manage your NFT's.

![Screenshot_20220526_161411](https://user-images.githubusercontent.com/106575637/171575897-86915824-f380-4b9e-8439-bc621ddb6774.png)

# Installation

## Step 1 : Add it in your root build.gradle at the end of repositories

<b>For old gradle version</b>
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
<b>For new gradle version</b>
Add the following code to your project's <b>settings.gradle</b> file.
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

## Step 2 : Add the dependency

```
dependencies {
		implementation 'com.github.Zbyteio:zbyte-android-sdk:v1.0.0'
	}
```

## Step 3: XML Layout

Just add the following code to your <b>xml layout</b> file

```
<com.zbyte.nftsdk.ZBytePlatform
        android:id="@+id/zbyteView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

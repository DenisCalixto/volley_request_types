# volley_request_types

If you are creating your own project from scratch:

1) enable internet access for the Androi emulator adding the following line to AndroiManifest.xml file:

    <uses-permission android:name="android.permission.INTERNET" />
    
2) if you are accessing an API without SSL (http instead of https), add the following line to AndroiManifest.xml file inside <application> tag:

    android:usesCleartextTraffic="true"
    
3) for using Volley, add the following line in build.gradle app file (not the project one) inside the dependencies list: 

    implementation 'com.android.volley:volley:1.1.1'

4) to add recycler view dependency (which we are using in this sample project), 

    implementation 'androidx.recyclerview:recyclerview:1.0.0'

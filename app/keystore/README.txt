1. Create signed apk using the keystore and once apk is generate Remove the META-INF folder in the release apk
2. jarsigner -verbose -keystore ./keystore/kanchisankara.keystore ./app-release.apk kanchisankara -digestalg SHA1 -sigalg SHA1withRSA
3. zipalign -v 4 app-release.apk app-release-signed.apk
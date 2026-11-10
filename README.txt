RESTBOOK - build guide

OPTION A: GitHub (sab se aasaan, kuch install nahi karna)
 1. github.com par free account banayein, naya repository banayein.
 2. Is poore folder ki files upload karein (.github folder samet).
 3. Repository mein "Actions" tab kholein -> "Build Restbook" -> "Run workflow".
 4. 5-10 minute baad run ke "Artifacts" section se download karein:
      Restbook-Windows-EXE      (Restbook.exe)
      Restbook-AndroidTV-APK    (app-debug.apk)

OPTION B: Apne computer par
 Windows EXE:   cd desktop ; npm install ; npm run dist   -> desktop/dist/Restbook.exe
 Android APK:   Android Studio mein "android" folder kholein -> Build > Build APK(s)
 Test only:     desktop/index.html ko seedha browser mein kholein.

APK ko TV par kaise daalein:
 USB drive se copy karke TV par file manager se install karein, ya
 adb connect <TV-IP> ; adb install app-debug.apk
 (TV settings mein "Unknown sources" allow karna hoga.)

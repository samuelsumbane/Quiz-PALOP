# WorkManager (usado internamente pelo Google Mobile Ads SDK)
-keep class * extends androidx.room.RoomDatabase
-keep class **_Impl { *; }
-keep class **_Impl$* { *; }
-keep class androidx.work.impl.WorkDatabase { *; }
-dontwarn androidx.work.**
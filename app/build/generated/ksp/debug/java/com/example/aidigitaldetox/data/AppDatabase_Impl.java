package com.example.aidigitaldetox.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UsageDao _usageDao;

  private volatile RestrictedAppDao _restrictedAppDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `usage_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `date` TEXT NOT NULL, `durationInMillis` INTEGER NOT NULL, `openCount` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `behavior_profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `totalUsageTime` INTEGER NOT NULL, `emergencyUnlockCount` INTEGER NOT NULL, `challengesCompleted` INTEGER NOT NULL, `challengesFailed` INTEGER NOT NULL, `averageDifficulty` INTEGER NOT NULL, `addictionScore` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `duration` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `challenge_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `challengeType` TEXT NOT NULL, `difficulty` TEXT NOT NULL, `success` INTEGER NOT NULL, `timeTaken` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `emergency_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `packageName` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `restricted_apps` (`packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `dailyLimitMs` INTEGER NOT NULL, `todayUsageMs` INTEGER NOT NULL, `isLocked` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`packageName`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c295aa5e48f6d69166b099465881b01d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `usage_logs`");
        db.execSQL("DROP TABLE IF EXISTS `behavior_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `app_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `challenge_logs`");
        db.execSQL("DROP TABLE IF EXISTS `emergency_logs`");
        db.execSQL("DROP TABLE IF EXISTS `restricted_apps`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsageLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsUsageLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageLogs.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageLogs.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageLogs.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageLogs.put("durationInMillis", new TableInfo.Column("durationInMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageLogs.put("openCount", new TableInfo.Column("openCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsageLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsageLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsageLogs = new TableInfo("usage_logs", _columnsUsageLogs, _foreignKeysUsageLogs, _indicesUsageLogs);
        final TableInfo _existingUsageLogs = TableInfo.read(db, "usage_logs");
        if (!_infoUsageLogs.equals(_existingUsageLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "usage_logs(com.example.aidigitaldetox.data.UsageLog).\n"
                  + " Expected:\n" + _infoUsageLogs + "\n"
                  + " Found:\n" + _existingUsageLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsBehaviorProfiles = new HashMap<String, TableInfo.Column>(8);
        _columnsBehaviorProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("totalUsageTime", new TableInfo.Column("totalUsageTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("emergencyUnlockCount", new TableInfo.Column("emergencyUnlockCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("challengesCompleted", new TableInfo.Column("challengesCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("challengesFailed", new TableInfo.Column("challengesFailed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("averageDifficulty", new TableInfo.Column("averageDifficulty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBehaviorProfiles.put("addictionScore", new TableInfo.Column("addictionScore", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBehaviorProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBehaviorProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBehaviorProfiles = new TableInfo("behavior_profiles", _columnsBehaviorProfiles, _foreignKeysBehaviorProfiles, _indicesBehaviorProfiles);
        final TableInfo _existingBehaviorProfiles = TableInfo.read(db, "behavior_profiles");
        if (!_infoBehaviorProfiles.equals(_existingBehaviorProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "behavior_profiles(com.example.aidigitaldetox.data.BehaviorProfile).\n"
                  + " Expected:\n" + _infoBehaviorProfiles + "\n"
                  + " Found:\n" + _existingBehaviorProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsAppSessions = new HashMap<String, TableInfo.Column>(5);
        _columnsAppSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppSessions = new TableInfo("app_sessions", _columnsAppSessions, _foreignKeysAppSessions, _indicesAppSessions);
        final TableInfo _existingAppSessions = TableInfo.read(db, "app_sessions");
        if (!_infoAppSessions.equals(_existingAppSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "app_sessions(com.example.aidigitaldetox.data.AppSession).\n"
                  + " Expected:\n" + _infoAppSessions + "\n"
                  + " Found:\n" + _existingAppSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsChallengeLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsChallengeLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeLogs.put("challengeType", new TableInfo.Column("challengeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeLogs.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeLogs.put("success", new TableInfo.Column("success", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeLogs.put("timeTaken", new TableInfo.Column("timeTaken", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChallengeLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChallengeLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChallengeLogs = new TableInfo("challenge_logs", _columnsChallengeLogs, _foreignKeysChallengeLogs, _indicesChallengeLogs);
        final TableInfo _existingChallengeLogs = TableInfo.read(db, "challenge_logs");
        if (!_infoChallengeLogs.equals(_existingChallengeLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "challenge_logs(com.example.aidigitaldetox.data.ChallengeLog).\n"
                  + " Expected:\n" + _infoChallengeLogs + "\n"
                  + " Found:\n" + _existingChallengeLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsEmergencyLogs = new HashMap<String, TableInfo.Column>(3);
        _columnsEmergencyLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmergencyLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmergencyLogs.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmergencyLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmergencyLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmergencyLogs = new TableInfo("emergency_logs", _columnsEmergencyLogs, _foreignKeysEmergencyLogs, _indicesEmergencyLogs);
        final TableInfo _existingEmergencyLogs = TableInfo.read(db, "emergency_logs");
        if (!_infoEmergencyLogs.equals(_existingEmergencyLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "emergency_logs(com.example.aidigitaldetox.data.EmergencyLog).\n"
                  + " Expected:\n" + _infoEmergencyLogs + "\n"
                  + " Found:\n" + _existingEmergencyLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsRestrictedApps = new HashMap<String, TableInfo.Column>(6);
        _columnsRestrictedApps.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestrictedApps.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestrictedApps.put("dailyLimitMs", new TableInfo.Column("dailyLimitMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestrictedApps.put("todayUsageMs", new TableInfo.Column("todayUsageMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestrictedApps.put("isLocked", new TableInfo.Column("isLocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestrictedApps.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestrictedApps = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestrictedApps = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestrictedApps = new TableInfo("restricted_apps", _columnsRestrictedApps, _foreignKeysRestrictedApps, _indicesRestrictedApps);
        final TableInfo _existingRestrictedApps = TableInfo.read(db, "restricted_apps");
        if (!_infoRestrictedApps.equals(_existingRestrictedApps)) {
          return new RoomOpenHelper.ValidationResult(false, "restricted_apps(com.example.aidigitaldetox.data.RestrictedApp).\n"
                  + " Expected:\n" + _infoRestrictedApps + "\n"
                  + " Found:\n" + _existingRestrictedApps);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c295aa5e48f6d69166b099465881b01d", "58c027221569db72a9bd23c4e88f5198");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "usage_logs","behavior_profiles","app_sessions","challenge_logs","emergency_logs","restricted_apps");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `usage_logs`");
      _db.execSQL("DELETE FROM `behavior_profiles`");
      _db.execSQL("DELETE FROM `app_sessions`");
      _db.execSQL("DELETE FROM `challenge_logs`");
      _db.execSQL("DELETE FROM `emergency_logs`");
      _db.execSQL("DELETE FROM `restricted_apps`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UsageDao.class, UsageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestrictedAppDao.class, RestrictedAppDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UsageDao usageDao() {
    if (_usageDao != null) {
      return _usageDao;
    } else {
      synchronized(this) {
        if(_usageDao == null) {
          _usageDao = new UsageDao_Impl(this);
        }
        return _usageDao;
      }
    }
  }

  @Override
  public RestrictedAppDao restrictedAppDao() {
    if (_restrictedAppDao != null) {
      return _restrictedAppDao;
    } else {
      synchronized(this) {
        if(_restrictedAppDao == null) {
          _restrictedAppDao = new RestrictedAppDao_Impl(this);
        }
        return _restrictedAppDao;
      }
    }
  }
}

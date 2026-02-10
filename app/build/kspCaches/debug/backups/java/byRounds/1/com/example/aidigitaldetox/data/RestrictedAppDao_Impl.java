package com.example.aidigitaldetox.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestrictedAppDao_Impl implements RestrictedAppDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RestrictedApp> __insertionAdapterOfRestrictedApp;

  private final EntityDeletionOrUpdateAdapter<RestrictedApp> __deletionAdapterOfRestrictedApp;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUsageAndLock;

  private final SharedSQLiteStatement __preparedStmtOfIncrementExtensionCount;

  private final SharedSQLiteStatement __preparedStmtOfSetWarningShown;

  public RestrictedAppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestrictedApp = new EntityInsertionAdapter<RestrictedApp>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `restricted_apps` (`packageName`,`appName`,`dailyLimitMs`,`todayUsageMs`,`isLocked`,`lastUpdated`,`extensionCount`,`warningShown`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RestrictedApp entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        statement.bindLong(3, entity.getDailyLimitMs());
        statement.bindLong(4, entity.getTodayUsageMs());
        final int _tmp = entity.isLocked() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getLastUpdated());
        statement.bindLong(7, entity.getExtensionCount());
        final int _tmp_1 = entity.getWarningShown() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
      }
    };
    this.__deletionAdapterOfRestrictedApp = new EntityDeletionOrUpdateAdapter<RestrictedApp>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `restricted_apps` WHERE `packageName` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RestrictedApp entity) {
        statement.bindString(1, entity.getPackageName());
      }
    };
    this.__preparedStmtOfUpdateUsageAndLock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE restricted_apps SET todayUsageMs = ?, isLocked = ?, lastUpdated = ? WHERE packageName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementExtensionCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE restricted_apps SET extensionCount = extensionCount + 1 WHERE packageName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetWarningShown = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE restricted_apps SET warningShown = 1 WHERE packageName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final RestrictedApp app,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRestrictedApp.insert(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RestrictedApp app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRestrictedApp.handle(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUsageAndLock(final String packageName, final long usage,
      final boolean isLocked, final long lastUpdated,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUsageAndLock.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, usage);
        _argIndex = 2;
        final int _tmp = isLocked ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, lastUpdated);
        _argIndex = 4;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateUsageAndLock.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementExtensionCount(final String packageName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementExtensionCount.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementExtensionCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setWarningShown(final String packageName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetWarningShown.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetWarningShown.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getRestrictedApp(final String packageName,
      final Continuation<? super RestrictedApp> $completion) {
    final String _sql = "SELECT * FROM restricted_apps WHERE packageName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RestrictedApp>() {
      @Override
      @Nullable
      public RestrictedApp call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDailyLimitMs = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMs");
          final int _cursorIndexOfTodayUsageMs = CursorUtil.getColumnIndexOrThrow(_cursor, "todayUsageMs");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLocked");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfExtensionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "extensionCount");
          final int _cursorIndexOfWarningShown = CursorUtil.getColumnIndexOrThrow(_cursor, "warningShown");
          final RestrictedApp _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDailyLimitMs;
            _tmpDailyLimitMs = _cursor.getLong(_cursorIndexOfDailyLimitMs);
            final long _tmpTodayUsageMs;
            _tmpTodayUsageMs = _cursor.getLong(_cursorIndexOfTodayUsageMs);
            final boolean _tmpIsLocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp != 0;
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final int _tmpExtensionCount;
            _tmpExtensionCount = _cursor.getInt(_cursorIndexOfExtensionCount);
            final boolean _tmpWarningShown;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWarningShown);
            _tmpWarningShown = _tmp_1 != 0;
            _result = new RestrictedApp(_tmpPackageName,_tmpAppName,_tmpDailyLimitMs,_tmpTodayUsageMs,_tmpIsLocked,_tmpLastUpdated,_tmpExtensionCount,_tmpWarningShown);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RestrictedApp>> getAllRestrictedApps() {
    final String _sql = "SELECT * FROM restricted_apps";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"restricted_apps"}, new Callable<List<RestrictedApp>>() {
      @Override
      @NonNull
      public List<RestrictedApp> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDailyLimitMs = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMs");
          final int _cursorIndexOfTodayUsageMs = CursorUtil.getColumnIndexOrThrow(_cursor, "todayUsageMs");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLocked");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfExtensionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "extensionCount");
          final int _cursorIndexOfWarningShown = CursorUtil.getColumnIndexOrThrow(_cursor, "warningShown");
          final List<RestrictedApp> _result = new ArrayList<RestrictedApp>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RestrictedApp _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDailyLimitMs;
            _tmpDailyLimitMs = _cursor.getLong(_cursorIndexOfDailyLimitMs);
            final long _tmpTodayUsageMs;
            _tmpTodayUsageMs = _cursor.getLong(_cursorIndexOfTodayUsageMs);
            final boolean _tmpIsLocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp != 0;
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final int _tmpExtensionCount;
            _tmpExtensionCount = _cursor.getInt(_cursorIndexOfExtensionCount);
            final boolean _tmpWarningShown;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWarningShown);
            _tmpWarningShown = _tmp_1 != 0;
            _item = new RestrictedApp(_tmpPackageName,_tmpAppName,_tmpDailyLimitMs,_tmpTodayUsageMs,_tmpIsLocked,_tmpLastUpdated,_tmpExtensionCount,_tmpWarningShown);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

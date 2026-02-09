package com.example.aidigitaldetox.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class UsageDao_Impl implements UsageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UsageLog> __insertionAdapterOfUsageLog;

  private final EntityInsertionAdapter<AppSession> __insertionAdapterOfAppSession;

  private final EntityInsertionAdapter<ChallengeLog> __insertionAdapterOfChallengeLog;

  private final EntityInsertionAdapter<EmergencyLog> __insertionAdapterOfEmergencyLog;

  private final EntityInsertionAdapter<BehaviorProfile> __insertionAdapterOfBehaviorProfile;

  public UsageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUsageLog = new EntityInsertionAdapter<UsageLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `usage_logs` (`id`,`packageName`,`appName`,`date`,`durationInMillis`,`openCount`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UsageLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPackageName());
        statement.bindString(3, entity.getAppName());
        statement.bindString(4, entity.getDate());
        statement.bindLong(5, entity.getDurationInMillis());
        statement.bindLong(6, entity.getOpenCount());
      }
    };
    this.__insertionAdapterOfAppSession = new EntityInsertionAdapter<AppSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_sessions` (`id`,`packageName`,`startTime`,`endTime`,`duration`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSession entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPackageName());
        statement.bindLong(3, entity.getStartTime());
        statement.bindLong(4, entity.getEndTime());
        statement.bindLong(5, entity.getDuration());
      }
    };
    this.__insertionAdapterOfChallengeLog = new EntityInsertionAdapter<ChallengeLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `challenge_logs` (`id`,`timestamp`,`challengeType`,`difficulty`,`success`,`timeTaken`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChallengeLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindString(3, entity.getChallengeType());
        statement.bindString(4, entity.getDifficulty());
        final int _tmp = entity.getSuccess() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getTimeTaken());
      }
    };
    this.__insertionAdapterOfEmergencyLog = new EntityInsertionAdapter<EmergencyLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `emergency_logs` (`id`,`timestamp`,`packageName`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EmergencyLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindString(3, entity.getPackageName());
      }
    };
    this.__insertionAdapterOfBehaviorProfile = new EntityInsertionAdapter<BehaviorProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `behavior_profiles` (`id`,`date`,`totalUsageTime`,`emergencyUnlockCount`,`challengesCompleted`,`challengesFailed`,`averageDifficulty`,`addictionScore`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BehaviorProfile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindLong(3, entity.getTotalUsageTime());
        statement.bindLong(4, entity.getEmergencyUnlockCount());
        statement.bindLong(5, entity.getChallengesCompleted());
        statement.bindLong(6, entity.getChallengesFailed());
        statement.bindLong(7, entity.getAverageDifficulty());
        statement.bindDouble(8, entity.getAddictionScore());
      }
    };
  }

  @Override
  public Object insertUsageLog(final UsageLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUsageLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAppSession(final AppSession session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppSession.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertChallengeLog(final ChallengeLog log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChallengeLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEmergencyLog(final EmergencyLog log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEmergencyLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBehaviorProfile(final BehaviorProfile profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBehaviorProfile.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UsageLog>> getUsageForDate(final String date) {
    final String _sql = "SELECT * FROM usage_logs WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usage_logs"}, new Callable<List<UsageLog>>() {
      @Override
      @NonNull
      public List<UsageLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDurationInMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationInMillis");
          final int _cursorIndexOfOpenCount = CursorUtil.getColumnIndexOrThrow(_cursor, "openCount");
          final List<UsageLog> _result = new ArrayList<UsageLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UsageLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final long _tmpDurationInMillis;
            _tmpDurationInMillis = _cursor.getLong(_cursorIndexOfDurationInMillis);
            final int _tmpOpenCount;
            _tmpOpenCount = _cursor.getInt(_cursorIndexOfOpenCount);
            _item = new UsageLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpDate,_tmpDurationInMillis,_tmpOpenCount);
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

  @Override
  public Flow<List<AppSession>> getSessionsSince(final long startTime) {
    final String _sql = "SELECT * FROM app_sessions WHERE startTime >= ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_sessions"}, new Callable<List<AppSession>>() {
      @Override
      @NonNull
      public List<AppSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final List<AppSession> _result = new ArrayList<AppSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppSession _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            _item = new AppSession(_tmpId,_tmpPackageName,_tmpStartTime,_tmpEndTime,_tmpDuration);
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

  @Override
  public Flow<List<ChallengeLog>> getChallengeHistory() {
    final String _sql = "SELECT * FROM challenge_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"challenge_logs"}, new Callable<List<ChallengeLog>>() {
      @Override
      @NonNull
      public List<ChallengeLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "success");
          final int _cursorIndexOfTimeTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "timeTaken");
          final List<ChallengeLog> _result = new ArrayList<ChallengeLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChallengeLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpChallengeType;
            _tmpChallengeType = _cursor.getString(_cursorIndexOfChallengeType);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final boolean _tmpSuccess;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSuccess);
            _tmpSuccess = _tmp != 0;
            final long _tmpTimeTaken;
            _tmpTimeTaken = _cursor.getLong(_cursorIndexOfTimeTaken);
            _item = new ChallengeLog(_tmpId,_tmpTimestamp,_tmpChallengeType,_tmpDifficulty,_tmpSuccess,_tmpTimeTaken);
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

  @Override
  public Flow<List<BehaviorProfile>> getRecentHistory() {
    final String _sql = "SELECT * FROM behavior_profiles ORDER BY id DESC LIMIT 30";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"behavior_profiles"}, new Callable<List<BehaviorProfile>>() {
      @Override
      @NonNull
      public List<BehaviorProfile> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalUsageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUsageTime");
          final int _cursorIndexOfEmergencyUnlockCount = CursorUtil.getColumnIndexOrThrow(_cursor, "emergencyUnlockCount");
          final int _cursorIndexOfChallengesCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "challengesCompleted");
          final int _cursorIndexOfChallengesFailed = CursorUtil.getColumnIndexOrThrow(_cursor, "challengesFailed");
          final int _cursorIndexOfAverageDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "averageDifficulty");
          final int _cursorIndexOfAddictionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "addictionScore");
          final List<BehaviorProfile> _result = new ArrayList<BehaviorProfile>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BehaviorProfile _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final long _tmpTotalUsageTime;
            _tmpTotalUsageTime = _cursor.getLong(_cursorIndexOfTotalUsageTime);
            final int _tmpEmergencyUnlockCount;
            _tmpEmergencyUnlockCount = _cursor.getInt(_cursorIndexOfEmergencyUnlockCount);
            final int _tmpChallengesCompleted;
            _tmpChallengesCompleted = _cursor.getInt(_cursorIndexOfChallengesCompleted);
            final int _tmpChallengesFailed;
            _tmpChallengesFailed = _cursor.getInt(_cursorIndexOfChallengesFailed);
            final int _tmpAverageDifficulty;
            _tmpAverageDifficulty = _cursor.getInt(_cursorIndexOfAverageDifficulty);
            final float _tmpAddictionScore;
            _tmpAddictionScore = _cursor.getFloat(_cursorIndexOfAddictionScore);
            _item = new BehaviorProfile(_tmpId,_tmpDate,_tmpTotalUsageTime,_tmpEmergencyUnlockCount,_tmpChallengesCompleted,_tmpChallengesFailed,_tmpAverageDifficulty,_tmpAddictionScore);
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

  @Override
  public Object getTotalUsageForDate(final String date,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(durationInMillis) FROM usage_logs WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

package com.lezyo.fsm.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lezyo.fsm.config.Constant;
import com.lidroid.xutils.util.LogUtils;

public class DbHelper {
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	public final static byte[] _writeLock = new byte[0];
	// 打开数据库
	public void OpenDB(Context context) {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	// 关闭数据库
	public void Close() {
		dbHelper.close();
		if(db!=null){
			db.close();
		}
	}

	/**
	 * 插入
	 * @param list
	 * @param table
	 *            表名
	 */
	public void Insert(List<ContentValues> list, String tableName) {
		synchronized (_writeLock) {
			db.beginTransaction();
			try {
				db.delete(tableName, null, null);
				for (int i = 0, len = list.size(); i < len; i++)
					db.insert(tableName, null, list.get(i));
				    db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}

	public DbHelper(Context context) {
		this.dbHelper = new DatabaseHelper(context);
	}

	/**
	 * 用于初始化数据库
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {
		// 定义数据库文件
		private static final String DB_NAME = Constant.DATA_BASE_NAME;
		// 定义数据库版本
		private static final int DB_VERSION = 1;
		private static Context context;
		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			DatabaseHelper.context = context;
		}
		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			createCacheDb(db);
            LogUtils.d("创建cache表成功");
		}
		/**
		 * 创建缓存表表
		 * @param db
		 */
		private void createCacheDb(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE "+Constant.TABLE_CACHE+" (");
			sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
			sb.append("time DATE NOT NULL DEFAULT (date('now')), ");
			sb.append("cachekey TEXT NOT NULL ,");
			sb.append("cachedata TEXT NOT NULL);");
			db.execSQL(sb.toString());
		}

		/**
		 * 更新版本时更新表
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			DropTable(db);
			onCreate(db);
             LogUtils.d("数据表更新");
//             System.out.println("这个是旧的版本号："+oldVersion);
		}

		/**
		 * 删除表
		 * @param db
		 */
		private void DropTable(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("DROP TABLE IF EXISTS " + Constant.TABLE_CACHE + ";");
			db.execSQL(sb.toString());
		}

		/**
		 * 清空数据表（仅清空数据）
		 * @param db
		 */
		public void ClearData(Context context){
			DatabaseHelper dbHelper = new DbHelper.DatabaseHelper(context);
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			StringBuilder sb=new StringBuilder();
			sb.append("DELETE FROM "+Constant.TABLE_CACHE);//清空缓存表
			db.execSQL(sb.toString());
		}
	}
}

package com.lezyo.fsm.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lezyo.fsm.config.Constant;
import com.lidroid.xutils.util.LogUtils;

public class CacheTabUtil {

	private DbHelper.DatabaseHelper		dbHelper;
	private SQLiteDatabase		db;
	public final static byte[] _writeLock = new byte[0];
	private Context context;
	public CacheTabUtil(Context context){
		dbHelper=new DbHelper.DatabaseHelper(context);
		db=dbHelper.getWritableDatabase();
		this.context = context;
	}
	public void closeDB(){
		dbHelper.close();
	}
	/*
	 * 判断是否已经存在
	 */
	private boolean exist(String cacheKey){
		String where="cachekey=?";
		Cursor cursor= db.query(Constant.TABLE_CACHE, null, where, new String[]{cacheKey}, null, null, null);
		boolean isExist= cursor!=null && cursor.moveToNext();
		cursor.close();
		return isExist;
	}
	/**
	 * 得到缓存的data数据
	 */
	public JSONObject getData2Cache(String cacheKey){
		String where="cachekey=?";
		String[] args={cacheKey};
		String data = null;
		Cursor cursor= db.query(Constant.TABLE_CACHE, null, where, args, null, null, null);
		if ( cursor != null &&  cursor.moveToFirst() ) {
			data= cursor.getString(cursor.getColumnIndex("cachedata"));
		}
		cursor.close();
		if (data != null) {
			JSONObject result = null;
			try {
				result = new JSONObject(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if ( result != null) {
				try {
					JSONObject dataJson = result.getJSONObject("data");
					return dataJson;
				} catch (JSONException e) {
					e.printStackTrace();
					return result;
				}
			}
		}
		return null;
	}
	/***
	 * 更新或者插入缓存数据
	 * @param cacheKey
	 * @param cacheData
	 */
	public void updateData2Cache (String cacheKey , String cacheData) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("cachedata", cacheData );
		contentValues.put("time",ParseDateToString(new Date()));
		synchronized(_writeLock){
			db.beginTransaction();
			try{
				boolean isExist = exist(cacheKey);
				if(isExist){
					db.update(Constant.TABLE_CACHE, contentValues, "cachekey=?", new String[]{cacheKey});
				}
				else {
					contentValues.put("cachekey", cacheKey );
					db.insert(Constant.TABLE_CACHE, null , contentValues );
				}
				db.setTransactionSuccessful();
			}catch(Exception ex){
				LogUtils.e("fav_insert", ex);
			}finally{
				db.endTransaction();
			}
		}
	}
   
	/**
	 * 删除过期数据
	 * @param favId
	 */
	public boolean delete4OutTime() {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(java.util.Calendar.DAY_OF_MONTH, -Constant.OUT_TIME_DAY);
		String where = " time <= ? ";
		String[] args = {ParseDateToString(calendar.getTime())};
		try{
			db.delete(Constant.TABLE_CACHE, where, args);
			return true;
		}catch(Exception ex){
			LogUtils.e("fav_delete", ex);
			return false;
		}
	}
	
	public void cleanData () {
		dbHelper.ClearData(context);
	}

	/**
	 * 将日期转换为字符
	 * @param date
	 * @return
	 */
	public static String ParseDateToString(Date date){
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
}

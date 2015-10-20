package com.lezyo.fsm;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.lezyo.fsm.activity.MainActivity;
import com.lezyo.fsm.config.Constant;
import com.lezyo.fsm.dao.CacheTabUtil;
import com.lezyo.fsm.utils.CommonUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class RentApplication extends Application {
	private Set<Activity> activityList = new LinkedHashSet<Activity>();
	private static RentApplication instance;
	public static String userAgent;
	public static RentApplication getInstance() {
		return instance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initConfig();
//		JPushInterface.setDebugMode(Constant.LOG_OPEN); 	// 设置开启日志,发布时请关闭日志
//		JPushInterface.init(this);     		// 初始化 JPush
		//imageLoader
		File cacheDir = StorageUtils.getCacheDirectory(this);
		//设置图片的缓冲目录
		cacheDir = new File(cacheDir , Constant.CACHE_BIG_PATH);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		//	    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		.threadPoolSize(4) // default
		.threadPriority(Thread.NORM_PRIORITY - 1) // default
		.tasksProcessingOrder(QueueProcessingType.FIFO) // default
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
		.memoryCacheSize(5* 1024 * 1024)
		.memoryCacheSizePercentage(13) // default
		.diskCache(new UnlimitedDiscCache(cacheDir)) // default
		//	    .diskCacheSize(50 * 1024 * 1024)
		//	    .diskCacheFileCount(100)
		.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		.imageDownloader(new BaseImageDownloader(this)) // default
		.imageDecoder(new BaseImageDecoder(true)) // default
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		//		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.execute(new CacheTask());
	}
	public void initConfig() {
		String debug = CommonUtils.getMetaDataStringValue(this, "DEBUG");
		Constant.LOG_OPEN = !CommonUtils.isEmpty(debug) && debug.startsWith("yes");
		if (Constant.LOG_OPEN) {
			Constant.logOpen();
		}
		else {
			Constant.logClose();
		}
		int release = CommonUtils.getMetaDataIntValue(this, "RELEASE");
		//如果为1 强制更改为正式环境
		if (release == 1) {
			Constant.BASE_URL = "http://www.lezyo.com/index.php/appapi/Process";
		}
		userAgent = "channel="+  CommonUtils.getChannel( this ,"UMENG_CHANNEL") + ",UUID=" + CommonUtils.getDeviceId(instance) + ",IMEI=" + CommonUtils.getIMEI(this) + ",MAC=" + CommonUtils.getMacAddress() +
				",IMSI=" + CommonUtils.createIMSI() + ",PRODUCT=" + CommonUtils.getProduct() + ",OsVerion=" + CommonUtils.getOsVersion() + ",AppVersion=" + CommonUtils.getVersionName(instance);
	}
	
	/**清理过期缓存*/
	private class CacheTask implements Runnable {
		@Override
		public void run() {
			CacheTabUtil util = new CacheTabUtil (RentApplication.this);
			util.delete4OutTime();
			util.closeDB();
		}
	}
	public void addActivity(Activity activity) {
		try {
			// 新加进来的Activity进行主题的设置
			synchronized (this) {
				activityList.add(activity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Activity getActivity(String simpleName) {
		Activity activity = null;
		synchronized (this) {
			if (activityList != null && activityList.size() > 0) {
				for (Activity a : activityList) {
					if (simpleName.equals(a.getClass().getSimpleName())) {
						activity = a;
						break;
					}
				}
			}
		}
		return activity;
	}	
	/**
	 * 获取栈中上一个activity的名字
	 * 用于查找avtivity的跳转来源
	 * */
	public String preActvityName() {
		if (activityList != null && activityList.size() > 1) {
			Object[] stack =  activityList.toArray();
			Activity activity = (Activity) stack[stack.length-2];
			if ( activity!= null) {
				return activity.getClass().getSimpleName();
			}
		}
		return null;
	}
	public Activity finishAll() {
		Activity activity = null;
		synchronized (this) {
			if (activityList != null && activityList.size() > 0) {
				for (Activity a : activityList) {
					a.finish();
				}
			}
		}
		return activity;
	}

	// 有Activity手动finish的时候需要将其引用在集合中删除
	public void removeActivity(Activity activity) {
		synchronized (this) {
			if (!activityList.isEmpty() && activityList.contains(activity))
				activityList.remove(activity);
		}
		System.gc();
	}
	//------用于jpush跳转－－
	/**
	 * 以jpush方式打开程序后的返回逻辑
	 * 返回
	 * @param activityclass对象 要跳转的activity
	 */
	public void backActivity (Class<?> activityClass ,Context context) {
		if (activityList != null && activityList.size() > 1) {
			Object[] stack =  activityList.toArray();
			Activity activity = (Activity) stack[stack.length-1];
			activity.finish();
			activityList.remove(activity);
			return;
		}
		if ( !findActivity(activityClass) ) {
			//如果主界面都没有被启动过，则启动主界面
			if (activityClass.getSimpleName().equals("UserCenterActivity")) {
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
			} 
			else {
				Intent intent = new Intent(context, activityClass);
				context.startActivity(intent);
			}
		}
	}
	/**查找指定acitivyt*/
	public boolean findActivity (Class<?> activityClass ) {
		boolean findIt = false;
		synchronized (this) {
			if (activityList != null && activityList.size() > 0) {
				for (int i = activityList.size() -1; i>=0 ; i--) {
					Object[] stack =  activityList.toArray();
					Activity tempActivity = (Activity) stack[i];
					String simpleName = tempActivity.getClass().getSimpleName();
					if (!simpleName.equals(activityClass.getSimpleName())) {
						tempActivity.finish();
					}
					else {
						findIt = true;
						break;
					}
				}
			}
		}
		return findIt;
	}
//
//	/**
//	 * 退出登录处理逻辑退出LEZYO账户统一调用*
//	 */
//	public void loginOutHandler (Activity acitvity) {
//		SharePrenceUtil.setLogin(this, false);
//		SharePrenceUtil.saveUserEntity(this,new UserEntity());
//		SharePrenceUtil.saveLocationStr( this , "");
//		//停止jpush服务
//		JPushInterface.stopPush(this);
//		//刷新清单信息
//		Constant.REFRESH_FLAG = true;
//		IMLoginout();
//		if ( acitvity != null && !acitvity.isFinishing()) {
//			acitvity.finish();
//		}
//		Intent intent = new Intent(Constant.LOGOUT_ACTION);
//		intent.putExtra("preAction", preActvityName());
//		instance.sendBroadcast(intent);
//	}
//
//	//------------------------------
//	/**lezyo登录成功处理逻辑*/
//	private void loginLezyoSuccess ( UserEntity bean ) {
//		//---------------------Lezyo账号登录成功处理逻辑-----------------------------
//		SharePrenceUtil.setLogin(instance, true);
//		SharePrenceUtil	.saveUserEntity( instance, bean);
//		//设置jpush
//		if (JPushInterface.isPushStopped(instance)) {
//			JPushInterface.resumePush(instance);
//		}
//		//@libin,登录成功后，将push注册成功的id给业务服务器
//		instance.startService(new Intent("com.lezyo.jpushRegID.bindAction"));
//		PushUtil.getInstance().setAlias( instance, bean.getUid() );
//		Intent intent = new Intent(Constant.LOGIN_ACTION);
//		intent.putExtra("preAction", preActvityName());
//		instance.sendBroadcast(intent);
//		//刷新清单信息
//		Constant.REFRESH_FLAG = true;
//		ToastUtil.show(instance, "欢迎回来");
//	}
}

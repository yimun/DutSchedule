package com.siwe.dutschedule.base;

public final class C {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// core settings (important)
	public static final class packageName{
		public static final String model = "com.siwe.dutschedule.model";
	}
	// preference name 
	public static final String PREFERENCES_NAME = "com.siwe.dutschedule.pref";
	// do show debug message
	public static final boolean DEBUG_MODE = true;
	
	public static final class dir {
		public static final String base				= "/sdcard/dutschedule";
		public static final String faces			= base + "/faces";
		public static final String images			= base + "/images";
	}
	
	public static final class api {
		public static String base = "http://yimutest.sinaapp.com/dutserver/index.php?r=";
		/*public static void setBase(String base) {
			api.base = base;
		}*/

		public static final String initsource       = "http://yimulinwei.sinaapp.com";
		public static final String config           = "/appconfig.php";
		public static final String download         = "/sources/Duthelper(video).apk";
		
		public static final String index			= "admin/index";
		public static final String pagelogin		= "admin/pagelogin";
		public static final String login		    = "admin/login";
		public static final String logout			= "admin/logout";
		public static final String userinfo      	= "admin/userinfo";
		
		public static final String schedule 	    = "edu/schedule";
		public static final String scorethis 	    = "edu/scorethis";
		public static final String scoreall 	    = "edu/scoreall";
		public static final String exam 	        = "edu/exam";
		public static final String defaultbbs       = "edu/defaultbbs";
		public static final String news             = "edu/news"; //TODO
		
		public static final String bbsunread 	    = "bbs/unread";
		public static final String comment 	  	    = "bbs/comment";
		public static final String getlist 	        = "bbs/getlist";
		public static final String unreadlist       = "bbs/unreadlist";
		public static final String bbssearch        = "bbs/search";
		public static final String delcomment       = "bbs/delcomment";
		
		public static final String doinform         = "extra/inform";
		
	}
	
	public static final class task {
		//net task
		public static final int index		     = 1001;
		public static final int pagelogin		 = 1002;
		public static final int login            = 1003;
		public static final int logout		     = 1004;
		public static final int userinfo         = 1005;

		public static final int scorethis        = 1006;
		public static final int scoreall         = 1007;
		public static final int schedule         = 1008;
		public static final int exam             = 1009;
		public static final int defaultbbs       = 1010;
		public static final int news             = 1016;
		
		public static final int bbsunread        = 1011;
		public static final int comment          = 1012;
		public static final int getlist          = 1013;
		public static final int scheduleunread   = 1014;
		public static final int unreadlist       = 1015;
		public static final int bbssearch        = 1016;
		public static final int delcomment       = 1018;
		
		public static final int doinform         = 1017;
		
		//local db task
		public static final int db_schedule      = 1101;
		public static final int db_exam      	 = 1102;
		public static final int db_scorethis 	 = 1103;
		public static final int db_scoreall  	 = 1104;
		public static final int db_getlist       = 1105;
		public static final int db_getunread     = 1106;
	}
	
	public static final class err {
		public static final String network			= "网络有点不给力";
		public static final String server			= "网络有点不给力";
		public static final String jsonFormat		= "服务器消息格式错误";
		public static final String auth             = "用户名或密码错误";
		public static final String emptydata        = "暂无数据，刷新看看";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// intent & action settings
	
	public static final class intent {
		public static final class action {
			public static final String EDITTEXT		= "com.siwe.dutschedule.EDITTEXT";
			public static final String EDITBLOG		= "com.siwe.dutschedule.EDITBLOG";
		}
	}
	
	public static final class action {
		public static final class edittext {
			public static final int CONFIG			= 2001;
			public static final int COMMENT			= 2002;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// additional settings
	
	public static final class web {
		public static final String base				= "http://192.168.9.122:8002";
		public static final String index			= base + "/index.php";
		public static final String gomap			= base + "/gomap.php";
	}
}
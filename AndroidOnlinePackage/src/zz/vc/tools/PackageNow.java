package zz.vc.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;


public class PackageNow {
	//记录每个分支git仓库的地址
	public static final String[] BRANCH_0 = {"/home/alex/QA/master/yourProject","master"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_1 = {"/home/alex/QA/branch1/yourProject","develop"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_2 = {"/home/alex/QA/branch2/yourProject","feature/APK-builder-2"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_3 = {"/home/alex/QA/branch3/yourProject","feature/APK-builder-3"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_4 = {"/home/alex/QA/branch4/yourProject","feature/APK-builder-4"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_5 = {"/home/alex/QA/branch5/yourProject","feature/APK-builder-5"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_6 = {"/home/alex/QA/branch6/yourProject","feature/APK-builder-contribution"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_7 = {"/home/alex/QA/branch7/yourProject","feature/APK-builder-personalization"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_8 = {"/home/alex/QA/branch8/yourProject","feature/APK-builder-social"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_9 = {"/home/alex/QA/branch9/yourProject","feature/APK-builder-alex"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_10 = {"/home/alex/QA/branch10/yourProject","feature/APK-builder-carlos"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_11 = {"/home/alex/QA/branch11/yourProject","feature/APK-builder-robin"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_12 = {"/home/alex/QA/branch12/yourProject","bugfix/xxx"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[][] BRANCHES = {BRANCH_0,BRANCH_1,BRANCH_2,BRANCH_3,BRANCH_4,BRANCH_5,BRANCH_6,BRANCH_7,BRANCH_8,BRANCH_9,BRANCH_10,BRANCH_11,BRANCH_12};
	public static final String ROOT_COMMIT = "46d8985";//可以作为各分支公共原点的commit号
	public static final String KEYSTORE_PASSWORD = "your keystore password";
	public static final String ALIAS = "your alias name";
	//用于签名apk的签名文件的路径
	public static final String KEY_PATH = "/home/alex/QA/file/key";
	public static final String GIT_ROOT = "https://username:password@bitbucket.org/xxx/yourProject.git";//外网的git 地址
	
	public static final String APK_PATH = "/app/build/outputs/apk";//进入项目目录后，gradle编译完成后输出apk的目录
	//下面是编译完后生成apk的文件名
	public static String APP_DEBUG = "app-debug.apk";//打了默认签名的apk,build命令有效
	public static String APP_UNSIGNED = "app-&&-release-unsigned.apk";//打了默认签名的apk,assmbleRelease命令有效
	public static final String APP_SIGNED = "app-signed.apk";//打了正式签名，但是没有4k对齐的apk名字
	public static final String APP_PUBLISH = "app-publish.apk";//打了正式签名，且对齐了的apk名字
	
	
	//替换文件的路径
	public static final String GIF_GRADLE = "/home/alex/QA/file/build.gradle";
	public static final String GIF_FILE = "/libraries/android-gif-drawable/build.gradle";
	//获取每个环境的名字，用于给文件命名的，下标是环境的代号
	public static final String[] environmentNames = {"Staging","Dev","Alpha1","Alpha2","Alpha3","Alpha4","Live"};
	public static final String GRADLE_HOME = "/opt/android-studio/gradle/gradle-2.14.1";
	
	public static final int totalCount = 997;//git编译控制台输出的总行数，用于判断进度
	public static String progress;//记录当前的打包进度
	public static boolean isPackaging = false;//记录带当前是否在打包，控制同一时刻只有一个打包进程，节省cpu，内存开销
	/**
	 * 根据分支号进行编译
	 * @param branch
	 * @return 返回值是命令行命令
	 */
	public static String[]  packageNow(int branch,String env) {//开始打包
		return new String[]{
			"cd "+BRANCHES[branch][0],//cd 到项目目录
			GRADLE_HOME+"/bin/gradle assemble&&Release".replace("&&", env)//正式进行编译
		};
	};
	/**
	 * 对apk进行签名
	 * @param branch
	 * @return 返回值是命令行命令
	 */
	public static String[] signKey(int branch,String env){
		return new String[]{
			"cd "+BRANCHES[branch][0]+APK_PATH,	
			"jarsigner -verbose -keystore "+KEY_PATH+" -storepass "+KEYSTORE_PASSWORD+" -signedjar "+APP_SIGNED+" -digestalg SHA1 -sigalg MD5withRSA "+APP_UNSIGNED.replace("&&", env.toLowerCase())+" "+ALIAS,
		};
	}
	/**
	 * 对app进行4k对齐
	 * @param branch
	 * @return 返回值是命令行命令
	 */
	public static String[] zipAlign(int branch){
		return new String[]{
				"cd "+BRANCHES[branch][0]+APK_PATH,	
				"zipalign -f -v 4 "+APP_SIGNED+" "+APP_PUBLISH,
			};
	}
	/**
	 * pull最新代码
	 * @param branch
	 * @return 返回值是命令行命令
	 */
	public static String[] gitPull(int branch){//pull 一个分支的代码
		return new String[]{
			"cd "+BRANCHES[branch][0],//cd 到项目目录
			"git reset --hard "+ROOT_COMMIT,
			"git clean -df",
//			"git checkout "+BRANCHES[branch][0]+"/*.java",
//			"git checkout "+BRANCHES[branch][0]+"/*.xml",
			"git pull "+GIT_ROOT + " "+BRANCHES[branch][1]
		};
	};
	
	/**
	 * 执行一条命令行指令
	 * @param orders 命令行命令
	 * @param callBack 控制台每输出一条反馈，会调用一次回调
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void runOneRow(String[] orders,ReadLineCallBack callBack) throws IOException{
		File wd = new File("/bin");
		System.out.println(wd); 
		Process proc = null; 
		try { 
		     proc = Runtime.getRuntime().exec("/bin/bash", null, wd); 
		} catch (IOException e) { 
		    e.printStackTrace(); 
		} if (proc != null) { 
//		     BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream())); 
			SequenceInputStream sis = new SequenceInputStream (proc.getInputStream (), proc.getErrorStream ());
			InputStreamReader isr = new InputStreamReader (sis, "UTF-8");
			BufferedReader br = new BufferedReader (isr);
		     PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true); 
		    for(String s:orders){
		    	System.out.println("command is::"+s);
		    	out.println(s);
		    }
		    out.println("exit"); 
		 try { 
			 String line; 
		     while ((line = br.readLine()) != null) {
		    	 callBack.readLine(line);
		        	System.out.println(line); 
		     } 
		   proc.waitFor();
		   br.close();
		   sis.close();
		   out.close(); 
		   proc.destroy(); 
		 } catch (Exception e) { 
		e.printStackTrace(); 
		} 
		}
	}
	/**
	 * 控制台每次返回文本后调用的回调接口
	 * @author Administrator
	 *
	 */
	public static interface ReadLineCallBack{
		void readLine(String line);
	}
	

	/**
	 * 开始打包函数
	 * @param appLocation 打包完成将apk发送到哪去的文件路径
	 * @param branch //分支序号
	 * @param environment //服务器环境序号
	 * @param sign //是否进行自动签名，如果进行签名那么签名完会再执行一步对齐操作
	 * @throws IOException
	 */
	public static void buildPackage(String appLocation,int branch,int environment,final boolean sign) throws IOException{
		System.out.println("即将打包的分支号是"+branch);
		progress ="正在进行编译";
		
		final int[]rowCount_progress = {0,0};//第0个记录当前是第几行，第1个记录进度百分比
		File giftargetFile = new File(BRANCHES[branch][0]+GIF_FILE);
		File gifSourceFile = new File(GIF_GRADLE);
		replaceFile(gifSourceFile,giftargetFile);
		runOneRow(packageNow(branch,environmentNames[environment]), new ReadLineCallBack() {
			
			public void readLine(String line) {
				// TODO Auto-generated method stub
				rowCount_progress[0]++;
				if(!progress.equals("编译完成") &&!progress.equals("error:编译失败"))progress = String.valueOf(Math.round((float)rowCount_progress[0]/(float)totalCount*100)+"%");
				if(line.startsWith(":")||line.startsWith("Reading")||line.startsWith("Note"))return;
				if(line.startsWith("BUILD SUCCESSFUL")){
					if(!sign)isPackaging = false;
					System.out.println("编译成功啦");
					progress = "编译完成";//如果不需要签名，那么现在已经成功了
				}else if(line.startsWith("BUILD FAILED") || "error:编译失败".equals(progress)){
					progress = "error:编译失败";
					isPackaging = false;
				}
				
			}
		});
		if(!progress.equals("编译完成")){
			System.out.println("没有编译成功"+progress);
			progress = "error:没有编译成功";
			return;
		}
		System.out.println("一共有"+rowCount_progress[0]+"行");
		if(sign){//如果需要签名
			System.out.println("准备进行签名"+BRANCHES[branch][0]);
			runOneRow(signKey(branch,environmentNames[environment]), new ReadLineCallBack() {
				
				public void readLine(String line) {
					// TODO Auto-generated method stub
					if(!line.startsWith("jar 已签名"))return;
					System.out.println("签名成功");
					progress = "签名成功！！";
				}
			});
			if(!progress.equals("签名成功！！")){//检测签名是否添加成功
				progress = "error:签名失败！！！";
				return;
			}
			progress = "正在进行apk对齐";
			//准备进行4k对齐
			runOneRow(zipAlign(branch), new ReadLineCallBack() {
				
				public void readLine(String line) {
					// TODO Auto-generated method stub
					if(line.startsWith("Unable to open")){
						System.out.println("4k对齐失败");
						progress = "4k对齐失败！！！";
						return;
					}
					if(!line.equals("Verification successful"))return;
					System.out.println("4k对齐成功");
					progress = "4k对齐成功";
				}
			});
			if(!progress.equals("4k对齐成功")){//检测4k对齐是否成功
				progress = "error:4k对齐失败";
				return;
			}
		}
		progress = "正在准备传送文件";
		File sourceFile = new File(BRANCHES[branch][0]+APK_PATH+"/"+(sign?APP_PUBLISH:APP_UNSIGNED.replace("&&", environmentNames[environment]).toLowerCase()));//需要签名和不需要签名给出的带key的app名字不一样
		System.out.println("源文件"+sourceFile.getAbsolutePath()+sourceFile.exists());
		System.out.println("源文件的大小是"+sourceFile.length());
		if(sourceFile.exists() && sourceFile.isFile() && sourceFile.length()>100000){
			File targetFile = new File(appLocation);
			if(replaceFile(sourceFile, targetFile))System.out.println("文件复制成功"+targetFile.length());
			progress = "succeed";//在这里算彻底的成功
		}else {
			progress = "error:失败";
		}
		isPackaging = false;
	}
	
	/**
	 * 一个简单的复制文件函数
	 * @param sourceFile
	 * @param targetFile
	 * @return
	 */
	public static boolean replaceFile(File sourceFile,File targetFile){
		long beginTime = System.currentTimeMillis();
		
		if(sourceFile==null||targetFile==null)return false;
		System.out.println("目标地址"+targetFile.getAbsolutePath());
		try {
			if(!targetFile.exists()){//如果目标文件不存在就新建
				targetFile.createNewFile();
			}else {//如果目标文件存在就删除，然后新建一个
				targetFile.delete();
				targetFile.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        FileInputStream fis;
        FileOutputStream fos;
		try {
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(targetFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
        byte[] b = new byte[1024];
        int len = 0;
        try {
	        while ((len = fis.read(b)) != -1) {
				fos.write(b, 0, len);
	        }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        try {
			fos.flush();
			fis.close();
	        fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        long endTime = System.currentTimeMillis();
        System.out.println("采用传统IO FileInputStream 读取，耗时："+ (endTime - beginTime));
        return true;
	}
	
}
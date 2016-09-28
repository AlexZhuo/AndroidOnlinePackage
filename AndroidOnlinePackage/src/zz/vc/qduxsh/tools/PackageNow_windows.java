package zz.vc.qduxsh.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;


public class PackageNow_windows {
	//记录每个分支git仓库的地址
	public static final String[] BRANCH_0 = {"E:\\QA\\master\\ProjectDir","master"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_1 = {"E:\\QA\\branch1\\ProjectDir","分支名"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_2 = {"E:\\QA\\branch2\\ProjectDir","分支名"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[] BRANCH_3 = {"E:\\QA\\branch3\\ProjectDir","分支名"};//第0个分支的相关信息，第一个是位置，第二个是分支号，第三个是编译器位置
	public static final String[][] BRANCHES = {BRANCH_0,BRANCH_1,BRANCH_2,BRANCH_3};
	
	public static final String GIT_ROOT = "https://AlexZhuo:123456@github.com/AlexZhuo/AlxImageLoader";//外网的git 地址,冒号前面是用户名，后面是密码,结尾一般带有.git
	
	public static final String APK_PATH = "\\app\\build\\outputs\\apk";//进入项目目录后，gradle编译完成后输出apk的目录
	//下面是编译完后生成apk的文件名
	public static final String APP_DEBUG = "app-debug.apk";//打了默认签名的apk
	public static final String APP_SIGNED = "app-signed.apk";//打了正式签名，但是没有4k对齐的apk名字
	public static final String APP_PUBLISH = "app-publish.apk";//打了正式签名，且对齐了的apk名字
	//用于签名apk的签名文件的路径
	public static final String KEY_PATH = "E:\\QA\\file\\key";
	
	//要根据不同服务器环境替换文件的路径
	public static final String LIVE_FILE = "E:\\QA\\file\\live\\Config.java";//记录live环境的java文件的路径，准备用于替换
	public static final String STAGING_FILE = "E:\\QA\\file\\staging\\Config.java";
	public static final String DEV_FILE = "E:\\QA\\file\\dev\\Config.java";
	public static final String OTHER1_FILE = "E:\\QA\\file\\other1\\Config.java";
	public static final String OTHER2_FILE = "E:\\QA\\file\\other2\\Config.java";
	public static final String OTHER3_FILE = "E:\\QA\\file\\other3\\Config.java";
	public static final String OTHER4_FILE = "E:\\QA\\file\\other4\\Config.java";
	public static final String[] environmentNames = {"Staging","Dev","Other1","Other2","Other3","Other4","Live"};//获取每个环境的名字，用于给文件命名的，下标是环境的代号
	public static final String[] ENVIRONMENTS = {STAGING_FILE,DEV_FILE,OTHER1_FILE,OTHER2_FILE,OTHER3_FILE,OTHER4_FILE,LIVE_FILE};//要根据不同环境替换文件的路径
	public static final String ENV_FILE = "\\app\\src\\main\\java\\conf\\Config.java";//要根据环境不同来动态被的项目里替换的java文件
	
	public static final int totalCount = 997;//git编译控制台输出的总行数，用于判断进度
	public static String progress;//记录当前的打包进度
	public static boolean isPackaging = false;//记录带当前是否在打包，控制同一时刻只有一个打包进程，节省cpu，内存开销
	/**
	 * 根据分支号进行编译
	 * @param branch
	 * @return 返回值是命令行命令
	 */
	public static String[]  packageNow(int branch) {//开始打包
		return new String[]{
			"cd "+BRANCHES[branch][0],//cd 到项目目录
			BRANCHES[branch][0].charAt(0)+":",
			"gradlew build"//正式进行编译
		};
	};
	/**
	 * 对apk进行签名
	 * @param branch
	 * @return 返回值是命令行命令
	 */
	public static String[] signKey(int branch){
		return new String[]{
			"cd "+BRANCHES[branch][0]+APK_PATH,	
			BRANCHES[branch][0].charAt(0)+":",
			//注意这里要替换密码，KEY_ALIAS，加密方式为你项目中的设置
			"jarsigner -verbose -keystore "+KEY_PATH+" -storepass 密码 -signedjar "+APP_SIGNED+" -digestalg SHA1 -sigalg MD5withRSA app-release-unsigned.apk KEY_ALLIAS",
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
				BRANCHES[branch][0].charAt(0)+":",
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
			BRANCHES[branch][0].charAt(0)+":",
			"git checkout *.java",
			"git pull "+GIT_ROOT + " "+BRANCHES[branch][1]
		};
	};
	
	/**
	 * 执行一条命令行指令
	 * @param orders 命令行命令
	 * @param callBack 控制台每输出一条反馈，会调用一次回调
	 * @throws IOException
	 */
	public static void runOneRow(String[] orders,ReadLineCallBack callBack) throws IOException{
		Process process = Runtime.getRuntime ().exec ("cmd");
		SequenceInputStream sis = new SequenceInputStream (process.getInputStream (), process.getErrorStream ());
		// next command
		OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
		InputStreamReader isr = new InputStreamReader (sis, "GBK");
		BufferedReader br = new BufferedReader (isr);
		BufferedWriter bw = new BufferedWriter (osw);
		for(String s : orders){
			System.out.println("待执行的语句是"+s);
			bw.write (s);
			bw.newLine ();
		}
		bw.flush ();
		bw.close ();
		osw.close ();
		// read
		String line = null;
		while (null != ( line = br.readLine () ))
		{
			System.out.println (line+"$$");
			callBack.readLine(line);
		}
		br.close ();
		isr.close ();
		process.destroy ();
	}
	/**
	 * 控制台每次返回文本后调用的回调接口
	 * @author Administrator
	 *
	 */
	public interface ReadLineCallBack{
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
		File targetEnvFile = new File(BRANCHES[branch][0]+ENV_FILE);//工作空间里的环境配置文件
		File sourceEnvFile = new File(ENVIRONMENTS[environment]);//写好环境的外头的配置文件
		copyFile(sourceEnvFile, targetEnvFile);
		runOneRow(packageNow(branch), new ReadLineCallBack() {
			
			public void readLine(String line) {
				// TODO Auto-generated method stub
				rowCount_progress[0]++;
				if(!progress.equals("编译完成") &&!progress.equals("error:编译失败"))progress = "Progress:"+String.valueOf(Math.round((float)rowCount_progress[0]/(float)totalCount*100)+"%");
				if(line.startsWith(":")||line.startsWith("Reading")||line.startsWith("Note"))return;
				if(line.startsWith("BUILD SUCCESSFUL")){
					if(!sign)isPackaging = false;
					System.out.println("编译成功啦");
					progress = "编译完成";//如果不需要签名，那么现在已经成功了
				}else if(line.startsWith("BUILD FAILED")){
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
			runOneRow(signKey(branch), new ReadLineCallBack() {
				
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
					if(!line.equals("Verification succesful"))return;
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
		File sourceFile = new File(BRANCHES[branch][0]+APK_PATH+"\\"+(sign?APP_PUBLISH:APP_DEBUG));//需要签名和不需要签名给出的带key的app名字不一样
		System.out.println("源文件"+sourceFile.getAbsolutePath()+sourceFile.exists());
		System.out.println("源文件的大小是"+sourceFile.length());
		if(sourceFile.exists() && sourceFile.isFile() && sourceFile.length()>100000){
			File targetFile = new File(appLocation);
			if(copyFile(sourceFile, targetFile))System.out.println("文件复制成功"+targetFile.length());
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
	public static boolean copyFile(File sourceFile,File targetFile){
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

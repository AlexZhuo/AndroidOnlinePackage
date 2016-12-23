package zz.vc.qduxsh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zz.vc.qduxsh.tools.PackageNow_Linux;


/**
 * Servlet implementation class PackageOL
 */
public class PackageOL extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PackageOL() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		System.out.println("有人访问在线打包servlet"+PackageNow_Linux.isPackaging);
		int branch = 0;//分支选择
		int environment = 0;//环境选择
		String fileNamePostfix;
		boolean needSign = false;
		try {
			branch = new Integer(request.getParameter("branch"));
			environment = new Integer(request.getParameter("environment"));
			fileNamePostfix = request.getParameter("postfix");
			if(request.getParameter("needSign")!=null && request.getParameter("needSign").length()>0)needSign = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("没有传来分支号！！！");
			return;
		}
		System.out.println("当前选择分支"+branch+"  环境"+environment+"  是否签名"+needSign);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH'h'mm'm'");
		String fileName = "Demo"+PackageNow_Linux.environmentNames[environment]+"_"+dateFormat.format(new Date(System.currentTimeMillis()));//QravedIIStaging_20160508_16h03m.apk
		if(fileNamePostfix != null && fileNamePostfix.length()>0) fileName = fileName+"_"+fileNamePostfix;
		String appLocation = getServletConfig().getServletContext().getRealPath("/").concat("release/").concat(fileName).concat(".apk");//将最终要给客户的文件的服务器路径发给打包类，让打包类打包完以后将文件复制到这个目录下
		
		PrintWriter out = response.getWriter();
		if(PackageNow_Linux.isPackaging){
			System.out.println("现在正在打包");
			out.write("packaging");
			out.flush();
			out.close();
			return;
		}else {
			out.write(fileName);//将准备放过去的文件名返回
		}
		out.flush();
		out.close();
		PackageNow_Linux.isPackaging = true;
		PackageNow_Linux.buildPackage(appLocation,branch,environment,needSign);
	}

}

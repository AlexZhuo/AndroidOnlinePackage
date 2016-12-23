package zz.vc.qduxsh.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import zz.vc.qduxsh.tools.PackageNow_Linux;


/**
 * Servlet implementation class PackageProcess
 */
public class PackageProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PackageProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static int mCount = 0;
    public static String lastProcess = "";
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		System.out.println("收到获取进度的请求"+PackageNow_Linux.progress);
		if(lastProcess != null && lastProcess.equals(PackageNow_Linux.progress))mCount++;
		else mCount = 0;
		lastProcess = PackageNow_Linux.progress;
		if(mCount > 100){
			PackageNow_Linux.progress ="error:编译失败";
			PackageNow_Linux.isPackaging = false;
		}
		System.out.println("当前进度是::::"+PackageNow_Linux.progress);
		if(PackageNow_Linux.progress==null){
			out.write("ready");
			out.flush();
			out.close();
			return;
		}
		out.write(PackageNow_Linux.progress);
		out.flush();
		out.close();
	}

}

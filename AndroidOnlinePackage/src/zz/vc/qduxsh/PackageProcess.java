package zz.vc.qduxsh;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import zz.vc.tools.PackageNow;

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
		System.out.println("收到获取进度的请求"+PackageNow.progress);
		if(lastProcess != null && lastProcess.equals(PackageNow.progress))mCount++;
		else mCount = 0;
		lastProcess = PackageNow.progress;
		if(mCount > 100){
			PackageNow.progress ="error:编译失败";
			PackageNow.isPackaging = false;
		}
		System.out.println("当前进度是::::"+PackageNow.progress);
		if(PackageNow.progress==null){
			out.write("ready");
			out.flush();
			out.close();
			return;
		}
		out.write(URLEncoder.encode(PackageNow.progress,"UTF-8"));
		out.flush();
		out.close();
	}

}

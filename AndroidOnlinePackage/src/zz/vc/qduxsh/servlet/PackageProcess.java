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
 * 通过单例（只能一个人同时打包）返回打包进度
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		System.out.println("收到获取进度的请求");
			System.out.println("当前进度是"+PackageNow_Linux.progress);
			if(PackageNow_Linux.progress==null)PackageNow_Linux.progress="当前没有打板任务";
			out.write(PackageNow_Linux.progress);
			out.flush();
			out.close();
	}

}

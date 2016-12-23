package zz.vc.qduxsh.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zz.vc.qduxsh.tools.PackageNow_Linux;
import zz.vc.qduxsh.tools.PackageNow_Linux.ReadLineCallBack;


/**
 * Servlet implementation class PullCodeServlet
 */
public class PullCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PullCodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		int branch = 0;
		final boolean[] status = {true} ;//记录是否pull成功
		if(PackageNow_Linux.isPackaging){
			PrintWriter writer = response.getWriter();
			writer.write("compiling");
			writer.flush();
			writer.close();
			return;
		}
		try {
			branch = new Integer(request.getParameter("branch"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("没有传来分支号！！！");
			status[0] = false;
		}
		PackageNow_Linux.isPackaging = true;
		PackageNow_Linux.runOneRow(PackageNow_Linux.gitPull(branch), new ReadLineCallBack() {
			
			public void readLine(String line) {
				// TODO Auto-generated method stub
				if(line.contains("fatal")||line.contains("error"))status[0] = false;
			}
		});
		PrintWriter writer = response.getWriter();
		writer.write(status[0]?"succeed":"failed");
		PackageNow_Linux.isPackaging = false;
		writer.flush();
		writer.close();
	}

}

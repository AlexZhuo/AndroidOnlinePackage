package zz.vc.qduxsh.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zz.vc.qduxsh.tools.PackageNow_Linux;

public class BranchList extends HttpServlet {

	/**
	 * 根据PackageNow那个类中记录分支及其目录的静态数组拉取所有的分支目录
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		String output = "";
		for(String[] ss : PackageNow_Linux.BRANCHES){
			output = output+ss[1]+",";
		}
		output = output.substring(0,output.length()-1);//remove ,
		System.out.println("branches=="+output);
		PrintWriter out = response.getWriter();
		out.write(output);
		out.flush();
		out.close();
	}

}

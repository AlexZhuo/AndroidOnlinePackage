package zz.vc.qduxsh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zz.vc.tools.PackageNow;
import zz.vc.tools.PackageNow.ReadLineCallBack;

public class RestartServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("success");
		out.flush();
		out.close();
		runRestart(new String[]{"/home/alex/tomcat_restart.sh"});
		
	}
	
	public static void runRestart(String[] orders) throws IOException{
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
		    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true); 
		    for(String s:orders){
		    	System.out.println("command is::"+s);
		    	out.println(s);
		    }
		    out.println("exit"); 
		}
	}

}

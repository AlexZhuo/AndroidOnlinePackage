package zz.vc.qduxsh.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zz.vc.qduxsh.tools.PackageNow_Linux;
import zz.vc.qduxsh.tools.PackageNow_Linux.ReadLineCallBack;

/**
 * 用于查询release文件夹下以前打好的文件目录
 */
public class ApkFilesList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApkFilesList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("有人访问最近打包文件servlet");
		String appLocation = getServletConfig().getServletContext().getRealPath("/").concat("release/");
		File releaseFolder = new File(appLocation);
		LinkedList<File> apps = new LinkedList<File>();
		File[] files = releaseFolder.listFiles();
		for(File f:files){
			if(f.isFile())apps.add(f);
		}
		Collections.sort(apps, new Comparator<File>() {

			public int compare(File o1, File o2) {
				// TODO Auto-generated method stub
				if(o1.lastModified()<o2.lastModified())return 1;
				else if(o1.lastModified()>o2.lastModified())return -1;
				return 0;
			}
		});
		StringBuffer fileNames = new StringBuffer();
		for(File f:apps){
			fileNames.append(f.getName()).append(",");
		}
		PrintWriter out = response.getWriter();
		out.print(fileNames.toString());
		out.flush();
		out.close();
		
	}

}

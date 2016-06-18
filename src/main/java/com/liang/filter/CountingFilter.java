package com.liang.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.liang.util.DateFormatUtil;
import com.liang.util.IPUtil;
import com.liang.vo.VisitorInfo;

public class CountingFilter implements Filter {

	private Logger logger = Logger.getLogger(CountingFilter.class);
	public static AtomicLong num;
	private static final String relativePath = "../logs/HelloSpringMVC/record.log";

	// private static final String relativePath = "record.log";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			File file = new File(relativePath);
			if (!file.exists()) {
				file.createNewFile();
				num = new AtomicLong(0);
			} else {
				ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(new FileInputStream(file));
					num = (AtomicLong) ois.readObject();
				} catch (FileNotFoundException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				} catch (ClassNotFoundException e) {
					logger.error(e);
				} finally {
					try {
						if (ois != null) {
							ois.close();
						}
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		num.incrementAndGet();
		String name = (String) request.getAttribute("name");
		System.out.println(name);
		VisitorInfo vi = new VisitorInfo(name,
				IPUtil.getIpAddr((HttpServletRequest) request),
				DateFormatUtil.formatDateToSeconds(new Date()));
		logger.info(vi.toString());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("destroy the CountingFilter...");
		File file = new File(relativePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(num);
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
}

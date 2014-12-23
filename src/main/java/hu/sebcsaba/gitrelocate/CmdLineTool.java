package hu.sebcsaba.gitrelocate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CmdLineTool {
	
	private InputStream exec(String... params) throws IOException {
		try {
			Process proc = Runtime.getRuntime().exec(params);
			proc.waitFor();
			String errorString = IOUtils.toString(proc.getErrorStream(), "UTF-8");
			if (proc.exitValue() != 0 || errorString.length()>0) {
				throw new IOException("Running "+Arrays.asList(params)+" returned "+proc.exitValue()+" and gave error: "+errorString);
			}
			return proc.getInputStream();
		}
		catch (InterruptedException ex) {
			throw new IOException(ex);
		}
	}
	
	public InputStream getStream(String... params) throws IOException {
		return exec(params);
	}

	public void run(String... params) throws IOException {
		exec(params);
	}
	
	public String getString(String... params) throws IOException {
		InputStream stdout = exec(params);
		return IOUtils.toString(stdout, "UTF-8");
	}
	
	public List<String> getStringList(String... params) throws IOException {
		String source = getString(params);
		return Arrays.asList(source.split("\\s+"));
	}

}
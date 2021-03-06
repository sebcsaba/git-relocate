package hu.sebcsaba.gitrelocate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CmdLineToolTest {

	@Test
	public void testRun() throws IOException {
		CmdLineTool tool = new CmdLineTool();
		tool.run("echo", "hello", "world");
	}
	
	@Test
	public void testStream() throws IOException {
		CmdLineTool tool = new CmdLineTool();
		InputStream result = tool.getStream("echo", "hello", "world");
		Assert.assertNotNull(result);
		Assert.assertEquals('h', result.read());
	}
	
	@Test
	public void testString() throws IOException {
		CmdLineTool tool = new CmdLineTool();
		String result = tool.getString("echo", "hello", "world");
		Assert.assertEquals("hello world\n", result);
	}
	
	@Test
	public void testStringList() throws IOException {
		CmdLineTool tool = new CmdLineTool();
		List<String> result = tool.getStringList("echo", "hello", "world");
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("hello", result.get(0));
		Assert.assertEquals("world", result.get(1));
	}
	
	@Test
	public void testUseCurrentDirectory() throws IOException {
		CmdLineTool tool = new CmdLineTool();
		String result = tool.getString("pwd").trim();
		Assert.assertEquals(System.getProperty("user.dir"), result);
	}
	
	@Test
	public void testSetCurrentDirectory() throws IOException {
		CmdLineTool tool = new CmdLineTool(new File("/tmp/"));
		String result = tool.getString("pwd").trim();
		Assert.assertEquals("/tmp", result);
	}
	
	@Test
	public void testInputString() throws IOException {
		CmdLineTool base = new CmdLineTool();
		CmdLineTool tool = base.withInput("hello world");
		String result = tool.getString("wc", "-w").trim();
		Assert.assertEquals("2", result);
	}
	
}

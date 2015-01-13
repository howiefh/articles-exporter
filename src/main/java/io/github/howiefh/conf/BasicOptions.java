package io.github.howiefh.conf;

import io.github.howiefh.conf.RendererRegister.RendererTuple;

import java.util.List;

public class BasicOptions {
	protected String outDir="download/";
	protected List<RendererTuple> rendererTuples;
	
	protected BasicOptions(){}
	public List<RendererTuple> getRendererTuples() {
		return rendererTuples;
	}
	public void setRendererTuples(List<RendererTuple> rendererTuples) {
		this.rendererTuples = rendererTuples;
	}
	public String getOutDir() {
		return outDir;
	}
	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}
	
	private static BasicOptions instance;
	public static BasicOptions getInstance() {
		if (null==instance) {
			instance = new BasicOptions();
		}
		return instance;
	}
}

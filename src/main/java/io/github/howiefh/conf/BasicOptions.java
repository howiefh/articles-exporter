package io.github.howiefh.conf;

import io.github.howiefh.conf.RendererRegister.RendererTuple;

import java.util.List;

public class BasicOptions {
	protected String outDir="download/";
	protected List<RendererTuple> rendererTuples;
	protected String title;
	protected String ruleName;

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
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	private static BasicOptions instance;
	public static BasicOptions getInstance() {
		if (null==instance) {
			instance = new BasicOptions();
		}
		return instance;
	}
}

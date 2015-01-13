package io.github.howiefh.conf;

import io.github.howiefh.renderer.api.Renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RendererRegister {
	private static Map<String, RendererTuple> renderers = new HashMap<String, RendererTuple>();
	private static List<String> types = new ArrayList<String>();
	public static void register(String type,Renderer renderer) {
		register(type, renderer, true);
	}
	public static void register(String type,Renderer renderer, boolean outputSingleFile) {
		RendererTuple renderertTuple = new RendererTuple(outputSingleFile,renderer);
		renderers.put(type, renderertTuple);
		types.add(type);
	}
	public static RendererTuple getRendererTuple(String type) {
		return renderers.get(type);
	}
	public static Renderer getRenderer(String type) {
		return renderers.get(type).renderer;
	}
	public static boolean isSingleFile(String type) {
		return renderers.get(type).isSingleFile;
	}
	public static boolean contains(String type) {
		return renderers.containsKey(type);
	}
	public static String types(){
		return types.toString();
	}
	public static class RendererTuple{
		public boolean isSingleFile;
		public Renderer renderer;
		public RendererTuple(boolean isSingleFile, Renderer renderer) {
			this.isSingleFile = isSingleFile;
			this.renderer = renderer;
		}
	}
}


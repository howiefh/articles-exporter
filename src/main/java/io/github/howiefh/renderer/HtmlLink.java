package io.github.howiefh.renderer;

import java.util.UUID;

public class HtmlLink {
	private String href;
	private String content;
	private LinkType type;
	private UUID uuid;
	public LinkType getType() {
		return type;
	}
	public void setType(LinkType type) {
		this.type = type;
	}
	
	public HtmlLink(String href) {
		this.href = href;
		this.uuid = UUID.randomUUID();
	}
	public HtmlLink(String href, LinkType type) {
		this(href);
		this.type = type;
	}
	public HtmlLink(String href, String content,LinkType type) {
		this(href,type);
		this.content = content;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public UUID getUuid() {
		return uuid;
	}
	@Override
	public String toString() {
		return "HtmlLink [href=" + href + 
				", content=" + content + 
				", type=" + type +
				", uuid=" + uuid +
				"]";
	}
}

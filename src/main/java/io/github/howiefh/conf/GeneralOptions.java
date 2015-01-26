package io.github.howiefh.conf;

public class GeneralOptions extends BasicOptions{
	protected String articleListPageLink;
	protected int pageCount;
	protected int startPage;
	
	
	protected GeneralOptions(){}
	public String getArticleListPageLink() {
		return articleListPageLink;
	}
	public void setArticleListPageLink(String articleListPageLink) {
		this.articleListPageLink = articleListPageLink;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	
	private static GeneralOptions instance;
	public static GeneralOptions getInstance() {
		if (null==instance) {
			instance = new GeneralOptions();
		}
		return instance;
	}
}

package wang.igood.db.core;

public class PageInfo {
	
	public Integer pageNum;
	public Integer pageSize;
	
	/**
	 * @param pageNum 必须大于0
	 * @param pageSize 必须大于0
	 * */
	public PageInfo(Integer pageNum, Integer pageSize) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

}

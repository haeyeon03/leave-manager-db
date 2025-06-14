package util;

import model.PageVO;

public class PageUtil {

	/**
	 * 페이지네이션
	 * 
	 * @param totalSize 전체 데이터 건수
	 * @param pageSize	한 페이지당 데이터 건수
	 * @param page		선택된 페이지
	 * @return pageVO 페이지 시작값과 끝값
	 */
	public static PageVO paginate(int totalSize, int pageSize, int page) {
		int start = pageSize * (page - 1) + 1;
		int end = Math.min(start + pageSize, totalSize);
		return new PageVO(start, end);
	}

	/**
	 * 총 페이지 수
	 * 
	 * @param totalSize 리스트의 크기
	 * @return 총 페이지
	 */
	public static int calculateTotalPage(int totalCount, int pageSize) {
		int totalPage = (totalCount / pageSize);
		if (totalCount % pageSize > 0) {
			totalPage = totalPage + 1;
		}
		return totalPage;
	}

}

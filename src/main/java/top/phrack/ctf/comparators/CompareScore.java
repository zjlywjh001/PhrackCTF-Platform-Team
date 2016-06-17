/**
 * 
 */
package top.phrack.ctf.comparators;


import java.util.Comparator;
import java.util.Date;

import top.phrack.ctf.pojo.RanklistObj;

/**
 * 用户按得分排序
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
public class CompareScore implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		long score1 = ((RanklistObj) o1).getuserobj().getScore();
		long score2 = ((RanklistObj) o2).getuserobj().getScore();
		Date submit1 = ((RanklistObj) o1).getLastSummit();
		Date submit2 = ((RanklistObj) o2).getLastSummit();
		if (score1>score2) {
			return -1;
		} else {
			if (score1<score2) {
				return 1;
			} else {
				if (submit1.before(submit2)) {
					return -1;
				} else {
					if (submit1.after(submit2)) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		}
	}

}

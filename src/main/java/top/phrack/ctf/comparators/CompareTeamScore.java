/**
 * 
 */
package top.phrack.ctf.comparators;

import java.util.Comparator;
import java.util.Date;

import top.phrack.ctf.pojo.TeamRankObj;

/**
 * 队伍
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
public class CompareTeamScore implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		long score1 = ((TeamRankObj) o1).getScore();
		long score2 = ((TeamRankObj) o2).getScore();
		Date submit1 = ((TeamRankObj) o1).getLastSummit();
		Date submit2 = ((TeamRankObj) o2).getLastSummit();
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

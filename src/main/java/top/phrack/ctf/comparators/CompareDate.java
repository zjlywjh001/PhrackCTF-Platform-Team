/**
 * 
 */
package top.phrack.ctf.comparators;

import java.util.Comparator;
import java.util.Date;

import top.phrack.ctf.pojo.News;

/**
 * 对日期排序
 *
 * @author Jarvis
 * @date 2016年4月1日
 */
public class CompareDate implements Comparator{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		Date begin = ((News)o1).getPosttime();
		Date end = ((News)o2).getPosttime();
		if (begin.after(end)) {
			return -1;
		} else {
			if (begin.before(end)) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}

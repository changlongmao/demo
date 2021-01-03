/**  
* @Title: OlapDataResponse.java  
* @Package com.fy.displaySystem.bean.response  
* @Description: Olap数据
* @author wxj
* @date 2019年3月14日  
* @version V1.0  
*/
package com.example.demo.entity;

import java.io.Serializable;
import java.util.List;

/**  
* @ClassName: OlapDataResponse  
* @Description: Olap数据
* @author wxj
* @date 2019年3月14日  
* @version V1.0  
*
*/
public class OlapDataResponse implements Serializable {
	/**
     * 有时根据x轴名称无法确定唯一id进行下一级数据钻取，需要多传个对应横轴的的Id列表
     */
    private List<String> ids;
    /**
     * 标题
     */
    private String title;

    /**
     * 横轴显示
     */
    private List<String> xAxis;

    /**
     * 图表的lengends选项卡
     */
    private List<String> legends;

    /**
     * 三维数据
     */
    private List<Object> series;

	/**
	 * @return the ids
	 */
	public List<String> getIds() {
		return ids;
	}

	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the xAxis
	 */
	public List<String> getxAxis() {
		return xAxis;
	}

	/**
	 * @param xAxis the xAxis to set
	 */
	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * @return the legends
	 */
	public List<String> getLegends() {
		return legends;
	}

	/**
	 * @param legends the legends to set
	 */
	public void setLegends(List<String> legends) {
		this.legends = legends;
	}

	/**
	 * @return the series
	 */
	public List<Object> getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(List<Object> series) {
		this.series = series;
	}
    
    
}

package com.cheetah.solr.model;

import java.io.Serializable;

import com.cheetah.solr.common.SolrConstant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * solr常用查询参数
 * Description: 
 * @author jason
 * 2018年11月30日
 * @version 1.0
 */
@Data
@ApiModel(description="常用查询条件封装类")
public class SolrQueryParam implements Serializable {
	 
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="集合名称", required=true)
	private String collection;
	@ApiModelProperty(value="查询字符串,默认查询所有\\*:\\*", example="name:张三", required=true)
	private String q;
	@ApiModelProperty(value="过虑查询,作用:在q查询符合结果中同时是fq查询符合的", example="q=Name:张三&fq=CreateDate:[20081001 TO 20091031]")
	private String fq;
	@ApiModelProperty(value="排序,格式:sort=\\<field name\\>+\\<desc|asc\\>\\[,\\<field name\\>+\\<desc|asc\\>\\]", example="（score desc, price asc）表示先\"score\" 降序,再\"price\"升序,默认是相关性降序")
	private String sort;
	@ApiModelProperty(value="返回第一条记录在完整找到结果中的偏移位置,0开始,一般分页用")
	private Integer start;
	@ApiModelProperty(value="指定返回结果最多有多少条记录，配合start来实现分页")
	private Integer rows;
	@ApiModelProperty(value="表示索引显示那些field,*表示所有field")
	private String fl;
	@ApiModelProperty(value="默认查询的field")
	private String df;
	@ApiModelProperty(value="指定输出格式,默认为json", allowableValues="json,xml,python,ruby,php,csv")
	private String wt;
	@ApiModelProperty(value="是否高亮")
	private boolean hl;
	@ApiModelProperty(name="hl.fl",value="高亮field")
	private String hlFl;
	@ApiModelProperty(name="hl.simple.pre",value="高亮前面的格式")
	private String hlSimplePre;	
	@ApiModelProperty(name="hl.simple.post",value="高亮后面的格式")
	private String hlSimplePost;	
	@ApiModelProperty(value="是否启动统计")
	private boolean facet;
	@ApiModelProperty(name="facet.field",value="统计的field")
	private String facetField;
	
	public static String getFieldName(String field) {
		if(field.equals("hlFl")) {
			return SolrConstant.QUERY_CONDITION_HL_FL;
		}
		if(field.equals("hlSimplePre")) {
			return SolrConstant.QUERY_CONDITION_HL_SIMPLE_PRE;
		}
		if(field.equals("hlSimplePost")) {
			return SolrConstant.QUERY_CONDITION_HL_SIMPLE_POST;
		}
		if(field.equals("facetField")) {
			return SolrConstant.QUERY_CONDITION_FACET_FIELD;
		}
		return field;
	}
	
}

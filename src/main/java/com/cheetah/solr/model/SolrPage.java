package com.cheetah.solr.model;

import java.io.Serializable;

import org.apache.solr.common.SolrDocumentList;

import lombok.Data;

@Data
public class SolrPage implements Serializable{
	
	public final static int DEFAULT_PAGE_NUMBER = 1;
	public final static int DEFAULT_PAGE_SIZE = 10;

	private static final long serialVersionUID = 1L;

	private long total;//总条数
	private int currPage;//当前页码
	private int pageSize;//每页条数
	private SolrDocumentList list;//数据
	
	public SolrPage(long total, int pageNumber, int pageSize, SolrDocumentList list) {
		this.total = total;
		this.currPage = pageNumber;
		this.pageSize = pageSize;
		this.list = list;
	}
	
	public long getTotalPage() {
		long m = total % pageSize;
		if(m == 0) {
			return total / pageSize;
		}
		return total / pageSize + 1;
	}
	
}

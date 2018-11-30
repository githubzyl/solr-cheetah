package com.cheetah.solr.model;

import java.io.Serializable;

import org.apache.solr.common.SolrDocumentList;

import lombok.Data;

@Data
public class SolrData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SolrDocumentList list;//数据
	
	public SolrData(SolrDocumentList list) {
		this.list = list;
	}
	
	public int getTotal() {
		return (null == list ? 0 : list.size());
	}
	
}

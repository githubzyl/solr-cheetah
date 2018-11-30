package com.cheetah.solr.common;

import java.util.List;
import java.util.Optional;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrConnection {
    
	/**
	 * 单一节点连接
	 * @param baseSolrUrl
	 * @return
	 */
	public static SolrClient getClient(String baseSolrUrl) {
		return new HttpSolrClient.Builder()
				   .withBaseSolrUrl(baseSolrUrl)
				   .withConnectionTimeout(10000)
				   .withSocketTimeout(6000)
				   .build();
	}

	/**
	 * 集群连接
	 * @param solrUrls
	 * @return
	 */
	public static CloudSolrClient getCloudClient(List<String> solrUrls) {
		return new CloudSolrClient.Builder(solrUrls)
				   .withConnectionTimeout(10000)
				   .withSocketTimeout(6000)
				   .build();
	}

	/**
	 * 集群连接
	 * @param zkHosts
	 * @param zkChroot
	 * @return
	 */
	public static CloudSolrClient getCloudClient(List<String> zkHosts, Optional<String> zkChroot) {
		return new CloudSolrClient.Builder(zkHosts, zkChroot)
				   .withConnectionTimeout(10000)
				   .withSocketTimeout(6000)
				   .build();
	}
	
	/**
	 * 提交并关闭连接
	 * @param solrClient
	 * @throws Exception
	 */
	public static void commitAndClose(SolrClient solrClient, String collection) throws Exception {
		if(null != collection && "".equals(collection)) {
			solrClient.commit(collection);
		}
		solrClient.close();
	}

}

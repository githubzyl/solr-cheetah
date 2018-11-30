package com.cheetah.solr.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheetah.solr.model.SolrPage;
import com.cheetah.solr.model.SolrQueryParam;
import com.cheetah.solr.service.IDocumentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/document")
@Api(description="Solr文档操作")
public class SolrDocumentController {

	public final static String SUCCESS = "SUCCESS";

	@Autowired
	private IDocumentService documentService;

	@PostMapping("/add/{collection}")
	@ApiOperation(value = "添加文档")
	public Object addDocument(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody @ApiParam(value = "文档对象Map", required = true) Map<String, Object> doc) throws Exception {
		documentService.add(collection, doc);
		return SUCCESS;
	}

	@PostMapping("/bathchAdd/{collection}")
	@ApiOperation(value = "批量添加文档")
	public Object addDocuments(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody @ApiParam(value = "文档对象MapList", required = true) List<Map<String, Object>> docs)
			throws Exception {
		documentService.batchAdd(collection, docs);
		return SUCCESS;
	}

	@PostMapping("/update/{collection}")
	@ApiOperation(value = "更新文档", notes = "更新文档时只要文档ID不为空，而且存在则更新")
	public Object updateDocument(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody @ApiParam(value = "文档对象Map", required = true) Map<String, Object> doc) throws Exception {
		documentService.update(collection, doc);
		return SUCCESS;
	}

	@PostMapping("/bathchUpdate/{collection}")
	@ApiOperation(value = "批量更新文档", notes = "更新文档时只要文档ID不为空，而且存在则更新")
	public Object updateDocuments(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody @ApiParam(value = "文档对象MapList", required = true) List<Map<String, Object>> docs)
			throws Exception {
		documentService.batchAdd(collection, docs);
		return SUCCESS;
	}

	@GetMapping("/remove/{collection}/{id}")
	@ApiOperation(value = "根据ID删除文档")
	public Object removeDocument(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@PathVariable @ApiParam(value = "文档ID", required = true) String id) throws Exception {
		documentService.remove(collection, id);
		return SUCCESS;
	}

	@GetMapping("/batchRemove/{collection}")
	@ApiOperation(value = "根据ID集合删除文档")
	public Object removeDocument(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody @ApiParam(value = "文档ID集合", required = true) List<String> ids) throws Exception {
		documentService.batchRemove(collection, ids);
		return SUCCESS;
	}

	@GetMapping("/removeByQuery/{collection}")
	@ApiOperation(value = "根据条件删除文档")
	public Object removeDocumentByQuery(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestParam @ApiParam(value = "删除条件", required = true) String query) throws Exception {
		documentService.removeByQuery(collection, query);
		return SUCCESS;
	}

	@GetMapping("/clear/{collection}")
	@ApiOperation(value = "清空集合")
	public Object clearCollection(@PathVariable @ApiParam(value = "集合名称", required = true) String collection)
			throws Exception {
		documentService.clear(collection);
		return SUCCESS;
	}

	@PostMapping("/query/{collection}")
	@ApiOperation(value = "根据条件查询某个集合内容", notes = "默认查询全部内容")
	public Object query(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody(required = false) @ApiParam(value = "查询条件Map", required = false) Map<String, String> queryParam)
			throws Exception {
		return documentService.query(collection, queryParam);
	}
	
	@PostMapping("/queryCount/{collection}")
	@ApiOperation(value = "根据条件查询某个集合的文档总数")
	public Object queryCount(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestBody(required = false) @ApiParam(value = "查询条件Map", required = false) Map<String, String> queryParam)
			throws Exception {
		return documentService.queryCount(collection, queryParam);
	}

	@PostMapping("/queryForPage/{collection}")
	@ApiOperation(value = "根据条件查询某个集合内容", notes = "默认查询全部内容")
	public Object queryForPage(@PathVariable @ApiParam(value = "集合名称", required = true) String collection,
			@RequestParam(defaultValue=""+SolrPage.DEFAULT_PAGE_NUMBER) @ApiParam(value = "页码,默认第1页") Integer pageNumber,
			@RequestParam(defaultValue=""+SolrPage.DEFAULT_PAGE_SIZE) @ApiParam(value = "每页条数,默认每页10条") Integer pageSize,
			@RequestBody(required=false) @ApiParam(value = "查询条件Map", required = false) Map<String, String> queryParam) throws Exception {
		pageNumber = (pageNumber <= 0 ? SolrPage.DEFAULT_PAGE_NUMBER : pageNumber);
		pageSize = (pageSize <= 0 ? SolrPage.DEFAULT_PAGE_SIZE : pageSize);
		return documentService.queryForPage(collection, pageNumber, pageSize, queryParam);
	}
	
	@PostMapping("/query")
	@ApiOperation(value = "根据常用条件查询某个集合内容", notes = "默认查询全部内容")
	public Object query(SolrQueryParam param) throws Exception{
		return documentService.query(param);
	}

}
